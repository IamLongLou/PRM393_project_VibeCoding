import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';
import '../models/customer.dart';
import '../models/bill.dart';
import '../models/user.dart';

class DatabaseHelper {
  static final DatabaseHelper instance = DatabaseHelper._init();
  static Database? _database;

  DatabaseHelper._init();

  Future<Database> get database async {
    if (_database != null) return _database!;
    _database = await _initDB('water_billing.db');
    return _database!;
  }

  Future<Database> _initDB(String filePath) async {
    final dbPath = await getDatabasesPath();
    final path = join(dbPath, filePath);

    return await openDatabase(
      path,
      version: 2,
      onCreate: _createDB,
      onUpgrade: _upgradeDB,
    );
  }

  Future<void> _createDB(Database db, int version) async {
    await db.execute('''
      CREATE TABLE customers (
        id INTEGER PRIMARY KEY,
        code TEXT NOT NULL UNIQUE,
        name TEXT NOT NULL,
        address TEXT NOT NULL,
        phone TEXT NOT NULL,
        currentReading INTEGER NOT NULL,
        status INTEGER NOT NULL
      )
    ''');

    await db.execute('''
      CREATE TABLE bills (
        id INTEGER PRIMARY KEY,
        customerId INTEGER NOT NULL,
        customerName TEXT,
        customerCode TEXT,
        billCode TEXT NOT NULL UNIQUE,
        date TEXT NOT NULL,
        oldReading INTEGER NOT NULL,
        newReading INTEGER NOT NULL,
        consumption REAL NOT NULL,
        unitPrice REAL NOT NULL,
        amount REAL NOT NULL,
        vat REAL NOT NULL,
        totalAmount REAL NOT NULL,
        imagePath TEXT,
        isSynced INTEGER NOT NULL,
        FOREIGN KEY (customerId) REFERENCES customers (id) ON DELETE CASCADE
      )
    ''');

    await db.execute('''
      CREATE TABLE user_session (
        username TEXT PRIMARY KEY,
        fullName TEXT NOT NULL,
        role TEXT NOT NULL,
        email TEXT,
        phone TEXT,
        token TEXT,
        lastLoginAt TEXT NOT NULL
      )
    ''');

    await _seedDatabase(db);
  }

  Future<void> _upgradeDB(Database db, int oldVersion, int newVersion) async {
    if (oldVersion < 2) {
      await _addColumnIfMissing(db, 'bills', 'customerName', 'TEXT');
      await _addColumnIfMissing(db, 'bills', 'customerCode', 'TEXT');
      await db.execute('''
        CREATE TABLE IF NOT EXISTS user_session (
          username TEXT PRIMARY KEY,
          fullName TEXT NOT NULL,
          role TEXT NOT NULL,
          email TEXT,
          phone TEXT,
          token TEXT,
          lastLoginAt TEXT NOT NULL
        )
      ''');
    }
  }

  Future<void> _addColumnIfMissing(Database db, String table, String column, String type) async {
    final columns = await db.rawQuery('PRAGMA table_info($table)');
    final exists = columns.any((c) => c['name'] == column);
    if (!exists) {
      await db.execute('ALTER TABLE $table ADD COLUMN $column $type');
    }
  }

  Future<void> _seedDatabase(Database db) async {
    final customers = [
      Customer(id: 1, code: 'KH001', name: 'Lưu Bị', address: '12-A Phố Huế, Hai Bà Trưng, Hà Nội', phone: '0912345001', currentReading: 125),
      Customer(id: 2, code: 'KH002', name: 'Quan Vũ', address: '88 Đường Láng, Đống Đa, Hà Nội', phone: '0987654002', currentReading: 80),
      Customer(id: 3, code: 'KH003', name: 'Trương Phi', address: '15/2 Trần Duy Hưng, Cầu Giấy, Hà Nội', phone: '0904444003', currentReading: 210, status: CollectionStatus.completed),
      Customer(id: 4, code: 'KH004', name: 'Gia Cát Lượng', address: 'Lạch Tray, Ngô Quyền, Hải Phòng', phone: '0911222004', currentReading: 45, status: CollectionStatus.reading),
      Customer(id: 5, code: 'KH005', name: 'Tào Tháo', address: 'Trần Hưng Đạo, TP. Bắc Ninh', phone: '0933555005', currentReading: 320),
    ];

    for (final customer in customers) {
      await db.insert('customers', customer.toMap(), conflictAlgorithm: ConflictAlgorithm.ignore);
    }
  }

  Future<List<Customer>> getAllCustomers() async {
    final db = await database;
    final result = await db.query('customers', orderBy: 'code ASC');
    return result.map(Customer.fromMap).toList();
  }

  Future<Customer?> getCustomerById(int id) async {
    final db = await database;
    final maps = await db.query('customers', where: 'id = ?', whereArgs: [id]);
    return maps.isEmpty ? null : Customer.fromMap(maps.first);
  }

  Future<int> upsertCustomer(Customer customer) async {
    final db = await database;
    return db.insert('customers', customer.toMap(), conflictAlgorithm: ConflictAlgorithm.replace);
  }

  Future<void> upsertCustomers(List<Customer> customers) async {
    final db = await database;
    await db.transaction((txn) async {
      for (final customer in customers) {
        await txn.insert('customers', customer.toMap(), conflictAlgorithm: ConflictAlgorithm.replace);
      }
    });
  }

  Future<int> updateCustomer(Customer customer) async {
    final db = await database;
    return db.update('customers', customer.toMap(), where: 'id = ?', whereArgs: [customer.id]);
  }

  Future<List<Customer>> searchCustomers(String query) async {
    final db = await database;
    if (query.isEmpty) return getAllCustomers();
    final result = await db.query(
      'customers',
      where: 'LOWER(name) LIKE ? OR LOWER(code) LIKE ?',
      whereArgs: ['%${query.toLowerCase()}%', '%${query.toLowerCase()}%'],
      orderBy: 'code ASC',
    );
    return result.map(Customer.fromMap).toList();
  }

  Future<List<Bill>> getBillsByCustomerId(int customerId) async {
    final db = await database;
    final result = await db.query(
      'bills',
      where: 'customerId = ?',
      whereArgs: [customerId],
      orderBy: 'date DESC',
    );
    return result.map(Bill.fromMap).toList();
  }

  Future<List<Bill>> getAllBills() async {
    final db = await database;
    final result = await db.query('bills', orderBy: 'date DESC');
    return result.map(Bill.fromMap).toList();
  }

  Future<List<Bill>> getUnsyncedBills() async {
    final db = await database;
    final result = await db.query('bills', where: 'isSynced = ?', whereArgs: [0], orderBy: 'date DESC');
    return result.map(Bill.fromMap).toList();
  }

  Future<int> insertBill(Bill bill) async {
    final db = await database;
    return db.insert('bills', bill.toMap(), conflictAlgorithm: ConflictAlgorithm.replace);
  }

  Future<void> upsertBills(List<Bill> bills) async {
    final db = await database;
    await db.transaction((txn) async {
      for (final bill in bills) {
        await txn.insert('bills', bill.toMap(), conflictAlgorithm: ConflictAlgorithm.replace);
      }
    });
  }

  Future<int> updateBill(Bill bill) async {
    final db = await database;
    return db.update('bills', bill.toMap(), where: 'id = ?', whereArgs: [bill.id]);
  }

  Future<void> markBillsAsSynced(List<Bill> bills) async {
    final db = await database;
    await db.transaction((txn) async {
      for (final bill in bills) {
        await txn.update(
          'bills',
          {'isSynced': 1},
          where: bill.id != null ? 'id = ?' : 'billCode = ?',
          whereArgs: [bill.id ?? bill.billCode],
        );
      }
    });
  }

  Future<void> saveSession(User user, String? token) async {
    final db = await database;
    await db.insert(
      'user_session',
      {
        ...user.toMap(),
        'token': token,
        'lastLoginAt': DateTime.now().toIso8601String(),
      },
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
  }

  Future<User?> getLastSession() async {
    final db = await database;
    final rows = await db.query('user_session', orderBy: 'lastLoginAt DESC', limit: 1);
    return rows.isEmpty ? null : User.fromMap(rows.first);
  }

  Future<void> clearSession() async {
    final db = await database;
    await db.delete('user_session');
  }
}

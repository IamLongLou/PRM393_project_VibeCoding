# Water Billing API

Backend Spring Boot cho app Flutter thu tiền nước.

## Yêu cầu

- Java 17+
- Maven 3.9+
- SQL Server đang chạy ở `localhost:1433`

## Tạo database

Chạy script:

```sql
database/sqlserver_schema_seed.sql
```

Script tạo database `WaterBillingDb`, bảng `customers`, `bills`, `app_users`, `tariff_tiers` và dữ liệu mẫu.

## Cấu hình kết nối

Sửa `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=WaterBillingDb;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=YourStrong!Passw0rd
```

## Chạy API

```bash
cd backend
mvn spring-boot:run
```

API chạy ở `http://localhost:8080`.

Swagger UI chạy ở:

```text
http://localhost:8080/swagger-ui.html
```

Khi khởi động, backend tự tạo tài khoản mẫu nếu chưa có:

- `admin` / `admin123`
- `nhanvien01` / `123456`
- `khachhang01` / `654321`
- `abc` / `123`

## Endpoint chính

```http
POST /api/auth/login
GET  /api/customers
GET  /api/customers?q=KH001
POST /api/customers
PUT  /api/customers/{id}
PATCH /api/customers/{id}/status
PATCH /api/customers/{id}/reading

GET  /api/bills
GET  /api/bootstrap
GET  /api/customers/{customerId}/bills
GET  /api/bills/unsynced
POST /api/customers/{customerId}/meter-readings
POST /api/sync/bills
PATCH /api/bills/{id}/synced
```

Ví dụ login:

```bash
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
```

Ví dụ ghi chỉ số và lập hóa đơn:

```bash
curl -X POST http://localhost:8080/api/customers/1/meter-readings ^
  -H "Content-Type: application/json" ^
  -d "{\"newReading\":150,\"imagePath\":\"/images/meter-1.jpg\"}"
```

JSON trả về dùng `camelCase` giống model Flutter hiện tại: `currentReading`, `customerId`, `billCode`, `totalAmount`, `isSynced`.

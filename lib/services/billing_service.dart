import '../models/bill.dart';

/// Lớp xử lý nghiệp vụ tính toán hóa đơn và quản lý trạng thái thanh toán
class BillingService {
  // --- BUSINESS RULES: BIỂU GIÁ NƯỚC LŨY KẾ ---
  static const double priceLevel1 = 5973;  // Bậc 1: 0-10 m3
  static const double priceLevel2 = 7052;  // Bậc 2: 10-20 m3
  static const double priceLevel3 = 8669;  // Bậc 3: 20-30 m3
  static const double priceLevel4 = 15929; // Bậc 4: Trên 30 m3
  
  static const double vatRate = 0.05;      // Thuế VAT: 5%
  static const double envFeeRate = 0.10;   // Phí bảo vệ môi trường: 10%

  /// Tính tiền nước theo công thức lũy kế bậc thang
  static double calculateAmount(int consumption) {
    if (consumption <= 0) return 0;
    
    double baseAmount = 0;
    if (consumption <= 10) {
      baseAmount = consumption * priceLevel1;
    } else if (consumption <= 20) {
      baseAmount = (10 * priceLevel1) + (consumption - 10) * priceLevel2;
    } else if (consumption <= 30) {
      baseAmount = (10 * priceLevel1) + (10 * priceLevel2) + (consumption - 20) * priceLevel3;
    } else {
      baseAmount = (10 * priceLevel1) + (10 * priceLevel2) + (10 * priceLevel3) + (consumption - 30) * priceLevel4;
    }

    double totalWithTaxes = baseAmount * (1 + vatRate + envFeeRate);
    return totalWithTaxes;
  }

  // --- STATE MANAGEMENT: QUẢN LÝ DỮ LIỆU OFFLINE ---
  
  // Danh sách các hóa đơn đã thu nhưng chưa đồng bộ (Lưu tạm trong bộ nhớ)
  static List<Bill> pendingBills = [];

  /// Thêm hóa đơn vào danh sách chờ đồng bộ
  static void addPendingBill(Bill bill) {
    pendingBills.add(bill);
  }

  /// Lấy lịch sử hóa đơn (Bao gồm dữ liệu mẫu và dữ liệu vừa ghi)
  static List<Bill> getHistory(int customerId) {
    List<Bill> history = [
      Bill(id: 1001, customerId: customerId, customerName: "Nguyễn Văn A", customerCode: "CUST001", billCode: "BILL001", date: DateTime(2023, 11), oldReading: 100, newReading: 112, consumption: 12, unitPrice: 7083.33, amount: 85000, vat: 4250, totalAmount: 89250, isSynced: true),
      Bill(id: 1002, customerId: customerId, customerName: "Nguyễn Văn A", customerCode: "CUST001", billCode: "BILL002", date: DateTime(2023, 12), oldReading: 112, newReading: 125, consumption: 13, unitPrice: 7546.15, amount: 98000, vat: 4900, totalAmount: 102900, isSynced: true),
    ];
    
    // Gộp thêm các hóa đơn mới ghi offline
    history.addAll(pendingBills.where((b) => b.customerId == customerId));
    return history;
  }

  /// Xóa dữ liệu sau khi đồng bộ thành công
  static void clearPendingData() {
    pendingBills.clear();
  }
}

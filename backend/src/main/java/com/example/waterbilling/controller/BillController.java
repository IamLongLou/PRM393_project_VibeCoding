package com.example.waterbilling.controller;

import com.example.waterbilling.dto.BillDto;
import com.example.waterbilling.dto.BootstrapResponse;
import com.example.waterbilling.dto.MeterReadingRequest;
import com.example.waterbilling.dto.SyncRequest;
import com.example.waterbilling.service.CustomerService;
import com.example.waterbilling.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.time.LocalDateTime;

@Tag(name = "Bills & Sync", description = "Lập hóa đơn nước, xem lịch sử hóa đơn và đồng bộ dữ liệu offline.")
@RestController
@RequestMapping("/api")
public class BillController {
    private final BillingService billingService;
    private final CustomerService customerService;

    public BillController(BillingService billingService, CustomerService customerService) {
        this.billingService = billingService;
        this.customerService = customerService;
    }

    @Operation(
            summary = "Tải dữ liệu offline ban đầu",
            description = """
                    API dùng sau khi đăng nhập online thành công hoặc khi người dùng bấm refresh dữ liệu.
                    App lấy customers và bills từ response này để lưu vào SQLite.
                    Khi mất mạng, app đọc lại dữ liệu từ SQLite và vẫn ghi chỉ số/lập hóa đơn offline được.
                    """
    )
    @ApiResponse(responseCode = "200", description = "Gói dữ liệu bootstrap cho app offline-first.")
    @GetMapping("/bootstrap")
    public BootstrapResponse bootstrap() {
        return new BootstrapResponse(LocalDateTime.now(), customerService.getAll(null), billingService.getAll());
    }

    @Operation(summary = "Lấy tất cả hóa đơn", description = "Trả về toàn bộ hóa đơn, sắp xếp mới nhất trước. Dùng cho màn hình lịch sử và thống kê tổng hợp.")
    @ApiResponse(responseCode = "200", description = "Danh sách hóa đơn.")
    @GetMapping("/bills")
    public List<BillDto> getAll() {
        return billingService.getAll();
    }

    @Operation(summary = "Lấy hóa đơn theo khách hàng", description = "Trả về lịch sử hóa đơn của một khách hàng, dùng cho màn hình customer history/detail.")
    @ApiResponse(responseCode = "200", description = "Danh sách hóa đơn của khách hàng.")
    @GetMapping("/customers/{customerId}/bills")
    public List<BillDto> getByCustomer(
            @Parameter(description = "ID khách hàng.", example = "1")
            @PathVariable Long customerId
    ) {
        return billingService.getByCustomer(customerId);
    }

    @Operation(summary = "Lấy hóa đơn chưa đồng bộ", description = "Trả về các hóa đơn có isSynced=false để hiển thị ở màn hình trung tâm đồng bộ.")
    @ApiResponse(responseCode = "200", description = "Danh sách hóa đơn chưa đồng bộ.")
    @GetMapping("/bills/unsynced")
    public List<BillDto> getUnsynced() {
        return billingService.getUnsynced();
    }

    @Operation(
            summary = "Ghi chỉ số mới và lập hóa đơn",
            description = """
                    API chính cho luồng ghi chỉ số nước.
                    Backend lấy currentReading hiện tại của khách hàng làm oldReading, nhận newReading từ app,
                    tính consumption = newReading - oldReading, tính amount, vat, totalAmount, tạo billCode mới,
                    lưu hóa đơn với isSynced=false và cập nhật currentReading của khách hàng.
                    """
    )
    @ApiResponse(responseCode = "200", description = "Lập hóa đơn thành công.")
    @ApiResponse(responseCode = "400", description = "newReading nhỏ hơn currentReading hoặc dữ liệu không hợp lệ.")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng.")
    @PostMapping("/customers/{customerId}/meter-readings")
    public BillDto createFromMeterReading(
            @Parameter(description = "ID khách hàng cần ghi chỉ số.", example = "1")
            @PathVariable Long customerId,
            @Valid @RequestBody MeterReadingRequest request
    ) {
        return billingService.createFromMeterReading(customerId, request);
    }

    @Operation(
            summary = "Đồng bộ danh sách hóa đơn offline",
            description = """
                    Nhận danh sách hóa đơn app đã lưu tạm trên thiết bị khi offline.
                    Nếu billCode đã tồn tại, backend cập nhật lại hóa đơn; nếu chưa có thì tạo mới.
                    Sau khi xử lý, backend đánh dấu các hóa đơn là isSynced=true.
                    """
    )
    @ApiResponse(responseCode = "200", description = "Đồng bộ thành công, trả về danh sách hóa đơn đã synced.")
    @ApiResponse(responseCode = "400", description = "Danh sách rỗng hoặc dữ liệu không hợp lệ.")
    @PostMapping("/sync/bills")
    public List<BillDto> syncBills(@Valid @RequestBody SyncRequest request) {
        return billingService.sync(request);
    }

    @Operation(summary = "Đánh dấu một hóa đơn đã đồng bộ", description = "Chuyển isSynced của hóa đơn sang true sau khi app/server xác nhận đã xử lý xong.")
    @ApiResponse(responseCode = "200", description = "Cập nhật thành công.")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy hóa đơn.")
    @PatchMapping("/bills/{id}/synced")
    public BillDto markSynced(
            @Parameter(description = "ID hóa đơn.", example = "1")
            @PathVariable Long id
    ) {
        return billingService.markSynced(id);
    }
}

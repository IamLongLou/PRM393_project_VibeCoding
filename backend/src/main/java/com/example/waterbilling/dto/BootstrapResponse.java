package com.example.waterbilling.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Gói dữ liệu tải xuống để app Flutter lưu vào SQLite và dùng offline.")
public record BootstrapResponse(
        @Schema(description = "Thời điểm server trả dữ liệu.", example = "2026-06-14T20:30:00")
        LocalDateTime serverTime,
        @Schema(description = "Danh sách khách hàng/công việc để app lưu local.")
        List<CustomerDto> customers,
        @Schema(description = "Danh sách hóa đơn lịch sử để app lưu local.")
        List<BillDto> bills
) {
}

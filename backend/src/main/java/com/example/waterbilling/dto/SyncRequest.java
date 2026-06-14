package com.example.waterbilling.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "Request đồng bộ nhiều hóa đơn offline từ app lên backend.")
public record SyncRequest(
        @Schema(description = "Danh sách hóa đơn cần đồng bộ. Mỗi phần tử cần có billCode, customerId, chỉ số và số tiền.")
        @Valid @NotEmpty List<BillDto> bills
) {
}

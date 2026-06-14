package com.example.waterbilling.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request cập nhật trạng thái thu tiền/ghi chỉ số của khách hàng.")
public record UpdateStatusRequest(
        @Schema(
                description = "Trạng thái theo enum Flutter: 0=PENDING/chờ xử lý, 1=READING/đang ghi chỉ số, 2=COMPLETED/đã hoàn thành.",
                example = "2"
        )
        @NotNull @Min(0) @Max(2)
        Integer status
) {
}

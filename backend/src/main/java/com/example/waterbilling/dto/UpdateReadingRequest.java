package com.example.waterbilling.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request cập nhật trực tiếp chỉ số hiện tại của khách hàng.")
public record UpdateReadingRequest(
        @Schema(description = "Chỉ số nước mới. Phải lớn hơn hoặc bằng currentReading hiện tại.", example = "150")
        @NotNull @Min(0)
        Integer newReading
) {
}

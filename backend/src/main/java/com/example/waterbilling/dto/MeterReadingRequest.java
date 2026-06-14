package com.example.waterbilling.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request ghi chỉ số nước mới và lập hóa đơn.")
public record MeterReadingRequest(
        @Schema(description = "Chỉ số nước mới đọc từ đồng hồ. Phải lớn hơn hoặc bằng currentReading hiện tại.", example = "150")
        @NotNull @Min(0) Integer newReading,
        @Schema(description = "Đường dẫn ảnh đồng hồ nước nếu app có chụp ảnh.", example = "/images/meter-1.jpg")
        String imagePath
) {
}

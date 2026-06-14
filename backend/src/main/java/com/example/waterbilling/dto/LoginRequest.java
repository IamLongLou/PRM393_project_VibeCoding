package com.example.waterbilling.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Thông tin đăng nhập của người dùng app.")
public record LoginRequest(
        @Schema(description = "Tên đăng nhập.", example = "admin")
        @NotBlank String username,
        @Schema(description = "Mật khẩu đăng nhập.", example = "admin123")
        @NotBlank String password
) {
}

package com.example.waterbilling.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Kết quả đăng nhập thành công.")
public record LoginResponse(
        @Schema(description = "Token demo để app lưu phiên đăng nhập. Backend hiện chưa bắt buộc token ở các API khác.", example = "YWRtaW46Y2Y0...")
        String token,
        @Schema(description = "Thông tin người dùng đăng nhập.")
        UserDto user
) {
}

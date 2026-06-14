package com.example.waterbilling.controller;

import com.example.waterbilling.dto.LoginRequest;
import com.example.waterbilling.dto.LoginResponse;
import com.example.waterbilling.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Đăng nhập và lấy thông tin người dùng cho app Flutter.")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Đăng nhập",
            description = """
                    Kiểm tra username/password trong bảng app_users.
                    Nếu hợp lệ, API trả về token demo và thông tin người dùng gồm username, fullName, role, email, phone.

                    Tài khoản mẫu sau khi chạy backend:
                    admin/admin123, nhanvien01/123456, khachhang01/654321, abc/123.
                    """
    )
    @ApiResponse(responseCode = "200", description = "Đăng nhập thành công.")
    @ApiResponse(responseCode = "400", description = "Sai tài khoản, mật khẩu hoặc request thiếu dữ liệu.")
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}

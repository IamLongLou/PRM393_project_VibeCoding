package com.example.waterbilling.service;

import com.example.waterbilling.dto.LoginRequest;
import com.example.waterbilling.dto.LoginResponse;
import com.example.waterbilling.dto.UserDto;
import com.example.waterbilling.entity.AppUser;
import com.example.waterbilling.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.UUID;

@Service
public class AuthService {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        AppUser user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Sai tài khoản hoặc mật khẩu"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Sai tài khoản hoặc mật khẩu");
        }

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString((user.getUsername() + ":" + UUID.randomUUID()).getBytes());
        return new LoginResponse(token, UserDto.from(user));
    }
}

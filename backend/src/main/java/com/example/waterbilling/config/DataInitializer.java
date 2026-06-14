package com.example.waterbilling.config;

import com.example.waterbilling.entity.AppUser;
import com.example.waterbilling.entity.UserRole;
import com.example.waterbilling.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createUser("admin", "admin123", "Quản Trị Viên", UserRole.ADMIN, "admin@water.com", "0987654321");
        createUser("nhanvien01", "123456", "Nguyễn Văn A", UserRole.STAFF, "nguyenvana@gmail.com", "0912345678");
        createUser("khachhang01", "654321", "Lê Minh Triết", UserRole.USER, "trietle@gmail.com", "0901234567");
        createUser("abc", "123", "Khách Hàng Mới", UserRole.USER, "abc@gmail.com", "0900000000");
    }

    private void createUser(String username, String password, String fullName, UserRole role, String email, String phone) {
        if (userRepository.findByUsername(username).isPresent()) {
            return;
        }
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setRole(role);
        user.setEmail(email);
        user.setPhone(phone);
        userRepository.save(user);
    }
}

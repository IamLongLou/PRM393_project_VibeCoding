package com.example.waterbilling.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI waterBillingOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Water Billing API")
                        .version("1.0.0")
                        .description("""
                                API backend cho ứng dụng Flutter thu tiền nước.

                                Luồng chính:
                                1. Nhân viên đăng nhập.
                                2. Lấy danh sách khách hàng cần ghi chỉ số.
                                3. Gửi chỉ số nước mới để backend lập hóa đơn.
                                4. Xem lịch sử hóa đơn hoặc đồng bộ các hóa đơn offline.
                                """)
                        .contact(new Contact()
                                .name("Water Billing Team")
                                .email("support@waterbilling.local"))
                        .license(new License().name("Internal project")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local development server")
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("Backend README")
                        .url("/README.md"));
    }
}

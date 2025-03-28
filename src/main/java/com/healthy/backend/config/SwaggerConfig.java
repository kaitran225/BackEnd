package com.healthy.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Health service API",
                version = "1.0",
                description = "API documentation for Health service Application",
                contact = @Contact(
                        name = "Health service Team",
                        email = "support@healthservice.com",
                        url = "https://www.cybriadev.com"
                )
        ),
        servers = {
                @Server(
                        description = "Local Environment",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Production Environment",
                        url = "https://api.cybriadev.com"
                ),
                @Server(
                        description = "Development Environment",
                        url = "https://ram-network-testing.up.railway.app"
                )
        }
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi healthServiceApi() {
        return GroupedOpenApi.builder()
                .group("Health Service API")
                .packagesToScan("com.healthy.backend.controller")
                .build();
    }
}
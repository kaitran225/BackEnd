package com.healthy.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.RequestHandledEvent;

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
                ),
                summary = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTczOTg0NzMyNywiZXhwIjoxNzM5OTMzNzI3fQ._-IpOX-qHVr73XaMCvkqrvAsgXmkZYUsceourKqAzdk"
        ),
        servers = {
                @Server(
                        description = "Local Environment",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Production Environment",
                        url = "https://api.cybriadev.com"
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
@Component
public class SwaggerConfig {

    @EventListener
    public void onWebRequest(RequestHandledEvent event) {
        String requestUrl = event.getDescription();

        if (requestUrl.contains("/swagger-ui/")) {
            System.out.println("Swagger UI accessed. Injecting CSS...");
            System.out.println("Injecting custom script...");
            System.setProperty("springdoc.swagger-ui.customCssUrl", "/style.css");
            System.setProperty("springdoc.swagger-ui.customJsUrl", "/style.js");

        }
    }
} 
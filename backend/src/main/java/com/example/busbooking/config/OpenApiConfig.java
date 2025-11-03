package com.bookbus.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI bookBusOpenAPI() {
        return new OpenAPI()
                // Global security – every endpoint will show lock icon
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))

                // JWT Bearer definition
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Provide a valid JWT token (from /api/auth/login)")))

                // API metadata
                .info(new Info()
                        .title("BookBus API")
                        .version("1.0.0")
                        .description("""
                                <b>Secure Bus Booking System</b><br>
                                • JWT Authentication<br>
                                • Role-based access (USER / ADMIN / OPERATOR)<br>
                                • Real-time seat availability<br>
                                • Full CRUD for routes, buses, bookings
                                """));
    }
}
package com.apparel.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI apparelManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Apparel Management System API")
                        .description("API documentation for the Apparel Management System that handles inventory and warehouse operations for an online apparel store")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Apparel Management Team")
                                .email("shrabonti.pal@accenture.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter JWT token")));
    }
}

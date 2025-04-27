package com.apparel.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
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
                                .email("shrabonti.pal@accenture.com")));
    }
}

package com.jubasbackend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI custom() {
        var info = new Info()
                .title("Jubas Backend")
                .version("2.0");

        var addSecurityItem = new SecurityRequirement()
                .addList("Access Token");

        var securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("""
                        Acesse a rota '/auth' com credenciais
                        
                        - e-mail: cliente@gmail.com
                        
                        - senha: 12345678"""
                );

        var components = new Components()
                .addSecuritySchemes("Access Token", securityScheme);

        return new OpenAPI()
                .info(info)
                .addSecurityItem(addSecurityItem)
                .components(components);
    }
}

package com.jubasbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI config() {
        var info = new Info()
                .title("Jubas Backend")
                .version("2.0");
        return new OpenAPI().info(info);
    }
}

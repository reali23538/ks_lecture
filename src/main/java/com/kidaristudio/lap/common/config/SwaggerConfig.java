package com.kidaristudio.lap.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "강연 신청 플랫폼",
                description = "강연 신청 플랫폼 API",
                version = "v1"))
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi backOfficeAPI() {
        String[] paths = {"/back-office/**"};

        return GroupedOpenApi.builder()
                .group("BackOffice API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi frontAPI() {
        String[] paths = {"/front/**"};

        return GroupedOpenApi.builder()
                .group("Front API")
                .pathsToMatch(paths)
                .build();
    }
}

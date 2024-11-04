package com.OEzoa.OEasy.infra.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OEasy-API")
                        .version("1.0")
                        .description("오이려좋아의 OEasy Project"));
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        String[] paths = {"/api/**", "/**"};
        String[] packagesToScan = {"com.OEzoa.OEasy.api"};
        return GroupedOpenApi.builder()
                .group("springdoc-openapi")
                .pathsToMatch(paths)
                .packagesToScan(packagesToScan)
                .build();
    }
}
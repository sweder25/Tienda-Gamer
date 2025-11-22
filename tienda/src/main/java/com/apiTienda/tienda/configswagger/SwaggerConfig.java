package com.apiTienda.tienda.configswagger;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EDUTECH API - Sistema Tienda")
                        .version("1.0.0")
                        .description("Api de Sistema para la Tienda Gamer"));
    }
}
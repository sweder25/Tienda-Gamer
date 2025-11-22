package com.apiTienda.tienda.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("usuarios", r -> r.path("/api/usuarios/**")
                .uri("http://localhost:8086"))
            .route("productos", r -> r.path("/api/producto/**")
                .uri("http://localhost:8083"))
            .route("ventas", r -> r.path("/api/ventas/**")
                .uri("http://localhost:8087"))
            .route("registro", r -> r.path("/api/registro/**")
                .uri("http://localhost:8084"))
            .route("ingreso", r -> r.path("/api/ingreso/**")
                .uri("http://localhost:8080"))
            .route("boleta", r -> r.path("/api/boleta/**")
                .uri("http://localhost:8088"))
            .build();

            
    }
}
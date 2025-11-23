package com.apiTienda.tienda.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final ServicesProperties servicesProperties;

    public GatewayConfig(ServicesProperties servicesProperties) {
        this.servicesProperties = servicesProperties;
    }

    //RouteLocator define las rutas del API Gateway
    //Cada ruta redirige a un servicio específico basado en el path
    //Por ejemplo, las solicitudes a /api/productos/** se redirigen al servicio de productos

    //@Bean indica que este método produce un bean gestionado por Spring
    //Esto significa que el RouteLocator devuelto será registrado en el contexto de la aplicación
    //el ejemplo mas claro es el RestTemplate en el TiendaController
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Ruta para INGRESO
                .route("ingreso", r -> r
                        .path("/api/ingreso/**")
                        .uri(servicesProperties.getIngreso().getUrl()))
                
                // Ruta para PRODUCTOS
                .route("productos", r -> r
                        .path("/api/productos/**")
                        .uri(servicesProperties.getProductos().getUrl()))
                
                // Ruta para REGISTRO
                .route("registro", r -> r
                        .path("/api/registro/**")
                        .uri(servicesProperties.getRegistro().getUrl()))
                
                // Ruta para USUARIOS
                .route("usuarios", r -> r
                        .path("/api/usuarios/**")
                        .uri(servicesProperties.getUsuarios().getUrl()))
                
                // Ruta para VENTAS
                .route("ventas", r -> r
                        .path("/api/ventas/**")
                        .uri(servicesProperties.getVentas().getUrl()))
                
                // Ruta para BOLETA
                .route("boleta", r -> r
                        .path("/api/boleta/**")
                        .uri(servicesProperties.getBoleta().getUrl()))
                
                .build();
    }
}
package com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

@RestController
@RequestMapping("/api/tienda")
@Tag(name = "Tienda", description = "Endpoints que orquestan otros servicios (inventario, productos)")
public class TiendaController {

    private final RestTemplate restTemplate;
    private final String inventarioBaseUrl;
    private final String productosBaseUrl;
    private final String registrosBaseUrl;
    private final String usuariosBaseUrl;
    private final String ventasBaseUrl;
    private static final Logger log = LoggerFactory.getLogger(TiendaController.class);

    public TiendaController(RestTemplate restTemplate,
                            @Value("${services.inventario.url}") String inventarioBaseUrl,
                            @Value("${services.productos.url}") String productosBaseUrl,
                            @Value("${services.registros.url}") String registrosBaseUrl,
                            @Value("${services.usuarios.url}") String usuariosBaseUrl,
                            @Value("${services.ventas.url}") String ventasBaseUrl) {
        this.restTemplate = restTemplate;
        this.inventarioBaseUrl = inventarioBaseUrl;
        this.productosBaseUrl = productosBaseUrl;
        this.registrosBaseUrl = registrosBaseUrl;
        this.usuariosBaseUrl = usuariosBaseUrl;
        this.ventasBaseUrl = ventasBaseUrl;
        
    }

    @Operation(summary = "Obtener detalles de producto", description = "Consulta al servicio de productos los detalles del producto.")
    @GetMapping("/producto/{id}")
    public ResponseEntity<?> obtenerDetalleProducto(@PathVariable Long id) {
        String url = productosBaseUrl + "/api/producto/" + id;
        log.info("Llamando a servicio productos: {}", url);
        try {
            String detalle = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok(detalle);
        } catch (HttpStatusCodeException ex) {
            log.error("Respuesta HTTP desde productos: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (ResourceAccessException ex) {
            log.error("Fallo de conexión al servicio productos: {}", ex.getMessage());
            return ResponseEntity.status(503).body("Servicio de productos no disponible");
        } catch (Exception ex) {
            log.error("Error inesperado llamando a productos: ", ex);
            return ResponseEntity.status(500).body("Error interno en gateway");
        }
    }

}

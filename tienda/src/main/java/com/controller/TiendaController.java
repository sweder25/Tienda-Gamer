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
@RequestMapping ("/api")
@Tag(name = "Tienda", description = "Endpoints que orquestan otros servicios (inventario, productos)")
public class TiendaController {

    private final RestTemplate restTemplate;
    private final String productosBaseUrl;
    private final String registrosBaseUrl;
    private final String usuariosBaseUrl;
    private final String ventasBaseUrl;
    private final String boletaBaseUrl;
    private static final Logger log = LoggerFactory.getLogger(TiendaController.class);

    public TiendaController(RestTemplate restTemplate,
                            @Value("${services.productos.url}") String productosBaseUrl,
                            @Value("${services.registros.url}") String registrosBaseUrl,
                            @Value("${services.usuarios.url}") String usuariosBaseUrl,
                            @Value("${services.ventas.url}") String ventasBaseUrl,
                            @Value("${services.boleta.url}") String boletaBaseUrl) {
        this.restTemplate = restTemplate;
        this.productosBaseUrl = productosBaseUrl;
        this.registrosBaseUrl = registrosBaseUrl;
        this.usuariosBaseUrl = usuariosBaseUrl;
        this.ventasBaseUrl = ventasBaseUrl;
        this.boletaBaseUrl = boletaBaseUrl;
    }

    @Operation(summary = "Obtener una lista de producto", description = "Consulta al servicio de productos los detalles del producto.")
    @GetMapping("/producto")
    public ResponseEntity<String> obtenerProductos() {
        String url = productosBaseUrl + "/api/producto";
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpStatusCodeException e) {
            log.error("Error al llamar al servicio de productos: {}", e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            log.error("Error de acceso al recurso del servicio de productos: {}", e.getMessage());
            return ResponseEntity.status(503).body("Servicio de productos no disponible");
        }
    }


    @Operation(summary = "Obtener registros", description = "Consulta al servicio de registros los detalles de los registros.")
    @GetMapping("/registro")
    public ResponseEntity<String> obtenerRegistros() {
        String url = registrosBaseUrl + "/api/registro";
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpStatusCodeException e) {
            log.error("Error al llamar al servicio de registros: {}", e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            log.error("Error de acceso al recurso del servicio de registros: {}", e.getMessage());
            return ResponseEntity.status(503).body("Servicio de registros no disponible");
        }
    }


    @Operation(summary = "Obtener usuarios", description = "Consulta al servicio de usuarios los detalles de los usuarios.")
    @GetMapping("/usuarios")
    public ResponseEntity<String> obtenerUsuarios() {
        String url = usuariosBaseUrl + "/api/usuarios";
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpStatusCodeException e) {
            log.error("Error al llamar al servicio de usuarios: {}", e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            log.error("Error de acceso al recurso del servicio de usuarios: {}", e.getMessage());
            return ResponseEntity.status(503).body("Servicio de usuarios no disponible");
        }
    }

    @Operation(summary = "Obtener ventas", description = "Consulta al servicio de ventas los detalles de las ventas.")
    @GetMapping("/ventas")
    public ResponseEntity<String> obtenerVentas() {
        String url = ventasBaseUrl + "/api/ventas";
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpStatusCodeException e) {
            log.error("Error al llamar al servicio de ventas: {}", e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            log.error("Error de acceso al recurso del servicio de ventas: {}", e.getMessage());
            return ResponseEntity.status(503).body("Servicio de ventas no disponible");
        }
    }

    @Operation(summary= "Obtener Boletas", description = "Consulta al servicio de boletas los detalles de las boletas.")
    @GetMapping("/boletas")
    public ResponseEntity<String> obtenerBoletas() {
        String url = ventasBaseUrl + "/api/boleta";
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpStatusCodeException e) {
            log.error("Error al llamar al servicio de boletas: {}", e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            log.error("Error de acceso al recurso del servicio de boletas: {}", e.getMessage());
            return ResponseEntity.status(503).body("Servicio de boletas no disponible");
        }
    }
}

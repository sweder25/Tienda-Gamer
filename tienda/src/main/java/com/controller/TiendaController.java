package com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/tienda")
@Tag(name = "Tienda Gateway", description = "API Gateway para todos los servicios")
@CrossOrigin(origins = "*")
public class TiendaController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("usuarios")
    @Operation(summary = "Obtener información de usuario desde el servicio de usuarios")
    public ResponseEntity<Map> getUsuarioInfo() {
        String url = "http://localhost:8086/api/usuarios/listar";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return response;

    }

    @GetMapping("boletas")
    @Operation(summary = "Obtener todas las boletas desde el servicio de boleta")
    public ResponseEntity<Map> getBoletaInfo() {
        String url = "http://localhost:8088/api/boletas";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return response;
    }

    
    @GetMapping("registro")
    @Operation(summary = "Obtener información de registro desde el servicio de registro")
    public ResponseEntity<Map> getRegistroInfo() {
        String url = "http://localhost:8084/api/registro/listar";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return response;
    }
    
    @GetMapping("productos")
    @Operation(summary = "Obtener información de productos desde el servicio de productos")
    public ResponseEntity<Map> getProductosInfo() {
        String url = "http://localhost:8083/api/productos";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return response;
    }
    
    @GetMapping("ventas")
    @Operation(summary = "Obtener información de ventas desde el servicio de ventas")
    public ResponseEntity<Map> getVentasInfo() {
        String url = "http://localhost:8087/api/ventas";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return response;
    }
    
}
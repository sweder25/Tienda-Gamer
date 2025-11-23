package com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tienda")
@Tag(name = "Tienda Gateway", description = "API Gateway para todos los servicios")
@CrossOrigin(origins = "*")
public class TiendaController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/health")
    @Operation(summary = "Verificar estado del gateway")


    // Health check simple para el API Gateway
    // Responde con el estado y URLs de los servicios
    // Esto ayuda a verificar que el gateway está funcionando y conoce las rutas de los servicios.
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Gateway funcionando correctamente");
        response.put("services", Map.of(
            "ingreso", "http://localhost:8080",
            "productos", "http://localhost:8083",
            "registro", "http://localhost:8084",
            "usuarios", "http://localhost:8086",
            "ventas", "http://localhost:8087",
            "boleta", "http://localhost:8088"
        ));
        return ResponseEntity.ok(response);
    }
}
package com.apiBoleta.boleta.controller;

import com.apiBoleta.boleta.model.Boleta;
import com.apiBoleta.boleta.model.DTO.BoletaRequest;
import com.apiBoleta.boleta.model.DTO.BoletaResponse;
import com.apiBoleta.boleta.service.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boleta")
@Tag(name = "Boleta", description = "API para gestión de boletas")
@CrossOrigin(origins = "*")
public class BoletaController {

    @Autowired
    private BoletaService boletaService;

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener boletas por usuario")
    public ResponseEntity<Map<String, Object>> obtenerBoletasPorUsuario(@PathVariable Long usuarioId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BoletaResponse> boletas = boletaService.obtenerBoletasPorUsuario(usuarioId);
            response.put("success", true);
            response.put("data", boletas);
            response.put("total", boletas.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener boleta por ID")
    public ResponseEntity<Map<String, Object>> obtenerBoletaPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            BoletaResponse boleta = boletaService.obtenerBoletaPorId(id);
            response.put("success", true);
            response.put("data", boleta);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todas las boletas")
    public ResponseEntity<Map<String, Object>> listarTodas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Boleta> boletas = boletaService.listarTodas();
            response.put("success", true);
            response.put("data", boletas);
            response.put("total", boletas.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
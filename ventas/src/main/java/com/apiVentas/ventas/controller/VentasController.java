package com.apiVentas.ventas.controller;

import com.apiVentas.ventas.model.Venta;
import com.apiVentas.ventas.model.DTO.VentaRequest;
import com.apiVentas.ventas.model.DTO.VentaResponse;
import com.apiVentas.ventas.service.VentaService;
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
@RequestMapping("/api/ventas")
@Tag(name = "Ventas", description = "API para gestión de ventas")
@CrossOrigin(origins = "*")
public class VentasController {

    @Autowired
    private VentaService ventaService;

    @PostMapping("/crear")
    @Operation(summary = "Crear nueva venta y generar boleta automáticamente")
    public ResponseEntity<Map<String, Object>> crearVenta(@RequestBody VentaRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            VentaResponse venta = ventaService.crearVenta(request);
            response.put("success", true);
            response.put("message", "Venta creada y boleta generada exitosamente");
            response.put("data", venta);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener venta por ID")
    public ResponseEntity<Map<String, Object>> obtenerVentaPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            VentaResponse venta = ventaService.obtenerVentaPorId(id);
            response.put("success", true);
            response.put("data", venta);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener ventas por usuario")
    public ResponseEntity<Map<String, Object>> obtenerVentasPorUsuario(@PathVariable Long usuarioId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<VentaResponse> ventas = ventaService.obtenerVentasPorUsuario(usuarioId);
            response.put("success", true);
            response.put("data", ventas);
            response.put("total", ventas.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todas las ventas")
    public ResponseEntity<Map<String, Object>> listarTodas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Venta> ventas = ventaService.listarTodas();
            response.put("success", true);
            response.put("data", ventas);
            response.put("total", ventas.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar venta")
    public ResponseEntity<Map<String, Object>> eliminarVenta(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            ventaService.eliminarVenta(id);
            response.put("success", true);
            response.put("message", "Venta eliminada exitosamente");
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
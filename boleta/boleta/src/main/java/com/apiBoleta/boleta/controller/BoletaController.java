package com.apiBoleta.boleta.controller;

import com.apiBoleta.boleta.model.Boleta;
import com.apiBoleta.boleta.service.BoletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boletas")
@CrossOrigin(origins = "*")
public class BoletaController {

    @Autowired
    private BoletaService boletaService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> generarBoleta(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== POST /api/boletas RECIBIDO ===");
            System.out.println("Datos recibidos: " + requestData);
            
            if (requestData.get("ventaId") == null) {
                response.put("success", false);
                response.put("message", "ventaId es requerido");
                return ResponseEntity.badRequest().body(response);
            }
            
            Long ventaId = Long.valueOf(requestData.get("ventaId").toString());
            System.out.println("VentaId parseado: " + ventaId);
            
            Boleta boleta = boletaService.generarBoleta(ventaId);
            
            System.out.println("=== BOLETA GENERADA ===");
            System.out.println("ID: " + boleta.getId());
            System.out.println("Número: " + boleta.getNumero());
            System.out.println("Total: " + boleta.getTotal());
            System.out.println("Cliente: " + boleta.getNombreCliente());
            
            response.put("success", true);
            response.put("message", "Boleta generada exitosamente");
            response.put("data", boleta);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            System.err.println("ERROR al generar boleta: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al generar boleta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Boleta boleta = boletaService.obtenerPorId(id);
            System.out.println("GET /{id} - Boleta Total: " + boleta.getTotal());
            response.put("success", true);
            response.put("data", boleta);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/venta/{ventaId}")
    public ResponseEntity<Map<String, Object>> obtenerPorVentaId(@PathVariable Long ventaId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Boleta boleta = boletaService.obtenerPorVentaId(ventaId);
            System.out.println("GET /venta/{ventaId} - Boleta Total: " + boleta.getTotal());
            response.put("success", true);
            response.put("data", boleta);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarTodas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Boleta> boletas = boletaService.listarTodas();
            System.out.println("=== GET /api/boletas ===");
            System.out.println("Total boletas encontradas: " + boletas.size());
            
            // Verificar que los totales se están enviando
            boletas.forEach(b -> {
                System.out.println("Boleta " + b.getId() + " - Total: " + b.getTotal());
            });
            
            response.put("success", true);
            response.put("data", boletas);
            response.put("total", boletas.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("ERROR en listarTodas: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
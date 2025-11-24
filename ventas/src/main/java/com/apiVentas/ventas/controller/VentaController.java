package com.apiVentas.ventas.controller;

import com.apiVentas.ventas.model.Venta;
import com.apiVentas.ventas.model.DetalleVenta;
import com.apiVentas.ventas.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private RestTemplate restTemplate;

    private static final String BOLETA_URL = "http://localhost:8088/api/boletas";

    @PostMapping("/api/ventas")
    public ResponseEntity<?> crearVenta(@RequestBody Map<String, Object> requestData) {
        System.out.println("========================================");
        System.out.println("POST /api/ventas RECIBIDO");
        System.out.println("Datos: " + requestData);
        System.out.println("========================================");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Venta venta = new Venta();
            venta.setUsuarioId(Long.valueOf(requestData.get("usuarioId").toString()));
            venta.setNombreCliente(requestData.get("nombreCliente").toString());
            venta.setApellidoCliente(requestData.getOrDefault("apellidoCliente", "").toString());
            venta.setEmailCliente(requestData.get("emailCliente").toString());
            venta.setDireccion(requestData.get("direccion").toString());
            venta.setRegion(requestData.getOrDefault("region", "").toString());
            venta.setCodigoPostal(requestData.getOrDefault("codigoPostal", "").toString());
            venta.setMetodoPago(requestData.get("metodoPago").toString());
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> productosData = (List<Map<String, Object>>) requestData.get("productos");
            List<DetalleVenta> detalles = new ArrayList<>();
            
            for (Map<String, Object> prodData : productosData) {
                DetalleVenta detalle = new DetalleVenta();
                detalle.setProductoId(Long.valueOf(prodData.get("productoId").toString()));
                detalle.setCantidad(Integer.valueOf(prodData.get("cantidad").toString()));
                detalles.add(detalle);
            }
            
            venta.setProductos(detalles);
            
            Venta ventaCreada = ventaService.crearVenta(venta);
            
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    generarBoleta(ventaCreada.getId());
                } catch (Exception e) {
                    System.err.println("Error al generar boleta: " + e.getMessage());
                }
            }).start();
            
            response.put("success", true);
            response.put("message", "Venta creada exitosamente");
            response.put("data", ventaCreada);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    private void generarBoleta(Long ventaId) {
        try {
            System.out.println("=== GENERANDO BOLETA PARA VENTA " + ventaId + " ===");
            
            Map<String, Object> boletaData = new HashMap<>();
            boletaData.put("ventaId", ventaId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(boletaData, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                BOLETA_URL,
                HttpMethod.POST,
                request,
                Map.class
            );

            System.out.println("Boleta generada exitosamente");

        } catch (Exception e) {
            System.err.println("Error al generar boleta: " + e.getMessage());
        }
    }

    @GetMapping("/api/ventas/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Venta venta = ventaService.obtenerPorId(id);
            response.put("success", true);
            response.put("data", venta);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
    }

    @GetMapping("/api/ventas/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerPorUsuario(@PathVariable Long usuarioId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Venta> ventas = ventaService.obtenerPorUsuarioId(usuarioId);
            response.put("success", true);
            response.put("data", ventas);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/api/ventas")
    public ResponseEntity<?> listarTodas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Venta> ventas = ventaService.listarTodas();
            response.put("success", true);
            response.put("data", ventas);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/api/ventas/health")
    public ResponseEntity<?> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "ventas-service");
        return ResponseEntity.ok(response);
    }
}
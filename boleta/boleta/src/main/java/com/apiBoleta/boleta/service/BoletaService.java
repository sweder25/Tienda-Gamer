package com.apiBoleta.boleta.service;

import com.apiBoleta.boleta.model.Boleta;
import com.apiBoleta.boleta.repository.BoletaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BoletaService {

    @Autowired
    private BoletaRepository boletaRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String VENTAS_URL = "http://localhost:8087/api/ventas";

    @Transactional
    public Boleta generarBoleta(Long ventaId) {
        try {
            System.out.println("=== GENERANDO BOLETA ===");
            System.out.println("Consultando venta ID: " + ventaId);
            
            String url = VENTAS_URL + "/" + ventaId;
            System.out.println("URL: " + url);
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            System.out.println("Response status: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());
            
            if (response.getBody() == null) {
                throw new RuntimeException("Response body es null");
            }
            
            Map<String, Object> responseBody = response.getBody();
            
            if (!responseBody.containsKey("data") || responseBody.get("data") == null) {
                throw new RuntimeException("No se encontró 'data' en la respuesta: " + responseBody);
            }
            
            Map<String, Object> ventaData = (Map<String, Object>) responseBody.get("data");
            System.out.println("Venta data: " + ventaData);
            
            Boleta boleta = new Boleta();
            boleta.setVentaId(ventaId);
            boleta.setNumero(generarNumeroBoleta());
            boleta.setFechaEmision(LocalDateTime.now());
            boleta.setEstado("EMITIDA");
            
            // Asignar total
            Object totalObj = ventaData.get("total");
            Double total = 0.0;
            if (totalObj instanceof Number) {
                total = ((Number) totalObj).doubleValue();
            }
            System.out.println("Total asignado: " + total);
            boleta.setTotal(total);
            
            // Datos del cliente
            boleta.setNombreCliente(ventaData.get("nombreCliente").toString());
            boleta.setEmailCliente(ventaData.get("emailCliente").toString());
            
            // Datos adicionales
            if (ventaData.containsKey("usuarioId") && ventaData.get("usuarioId") != null) {
                Object usuarioIdObj = ventaData.get("usuarioId");
                boleta.setUsuarioId(usuarioIdObj instanceof Number ? ((Number) usuarioIdObj).longValue() : null);
            }
            
            if (ventaData.containsKey("direccion") && ventaData.get("direccion") != null) {
                boleta.setDireccionEnvio(ventaData.get("direccion").toString());
            }
            
            if (ventaData.containsKey("metodoPago") && ventaData.get("metodoPago") != null) {
                boleta.setMetodoPago(ventaData.get("metodoPago").toString());
            } else {
                boleta.setMetodoPago("NO_ESPECIFICADO");
            }
            
            // Detalle de productos como JSON
            if (ventaData.containsKey("detalles") && ventaData.get("detalles") != null) {
                try {
                    String detalleJson = objectMapper.writeValueAsString(ventaData.get("detalles"));
                    boleta.setDetalleProductos(detalleJson);
                    System.out.println("Detalle productos guardado: " + detalleJson);
                } catch (Exception e) {
                    System.err.println("Error al serializar productos: " + e.getMessage());
                }
            }
            
            System.out.println("Guardando boleta...");
            System.out.println("Boleta antes de guardar - Total: " + boleta.getTotal());
            Boleta boletaGuardada = boletaRepository.save(boleta);
            System.out.println("Boleta guardada exitosamente: " + boletaGuardada.getNumero());
            System.out.println("Boleta guardada - Total: " + boletaGuardada.getTotal());
            
            return boletaGuardada;
            
        } catch (Exception e) {
            System.err.println("ERROR en generarBoleta: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al generar boleta: " + e.getMessage(), e);
        }
    }

    private String generarNumeroBoleta() {
        return "BOL-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    public Boleta obtenerPorId(Long id) {
        Boleta boleta = boletaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Boleta no encontrada con ID: " + id));
        System.out.println("Boleta obtenida por ID - Total: " + boleta.getTotal());
        return boleta;
    }

    public Boleta obtenerPorVentaId(Long ventaId) {
        Boleta boleta = boletaRepository.findByVentaId(ventaId)
            .orElseThrow(() -> new RuntimeException("Boleta no encontrada para venta ID: " + ventaId));
        System.out.println("Boleta obtenida por VentaId - Total: " + boleta.getTotal());
        return boleta;
    }

    public List<Boleta> listarTodas() {
        List<Boleta> boletas = boletaRepository.findAll();
        System.out.println("=== LISTANDO TODAS LAS BOLETAS ===");
        boletas.forEach(b -> System.out.println("Boleta ID: " + b.getId() + " - Total: " + b.getTotal()));
        return boletas;
    }
}
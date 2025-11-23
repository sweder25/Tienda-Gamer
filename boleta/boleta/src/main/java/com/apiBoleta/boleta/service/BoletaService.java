package com.apiBoleta.boleta.service;

import com.apiBoleta.boleta.model.Boleta;
import com.apiBoleta.boleta.model.DTO.BoletaRequest;
import com.apiBoleta.boleta.model.DTO.BoletaResponse;
import com.apiBoleta.boleta.model.DTO.UsuarioDTO;
import com.apiBoleta.boleta.model.DTO.VentaDTO;
import com.apiBoleta.boleta.repository.BoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BoletaService {

    @Autowired
    private BoletaRepository boletaRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${servicio.ventas.url:http://localhost:8087}")
    private String ventasServiceUrl;

    @Value("${servicio.registro.url:http://localhost:8084}")
    private String registroServiceUrl;

    public BoletaResponse generarBoleta(BoletaRequest request) {
        try {
            //Obtener información de la venta
            String ventaUrl = ventasServiceUrl + "/api/ventas/" + request.getVentaId();
            Map<String, Object> ventaResponse = restTemplate.getForObject(ventaUrl, Map.class);
            
            if (ventaResponse == null || !(Boolean) ventaResponse.get("success")) {
                throw new RuntimeException("Venta no encontrada");
            }
            
            Map<String, Object> ventaData = (Map<String, Object>) ventaResponse.get("data");
            Long usuarioId = ((Number) ventaData.get("usuarioId")).longValue();
            Double total = ((Number) ventaData.get("total")).doubleValue();
            
            //Obtener información del usuario desde REGISTRO
            String registroUrl = registroServiceUrl + "/api/registro/" + usuarioId;
            Map<String, Object> registroResponse = restTemplate.getForObject(registroUrl, Map.class);
            
            if (registroResponse == null || !(Boolean) registroResponse.get("success")) {
                throw new RuntimeException("Usuario no encontrado");
            }
            
            Map<String, Object> userData = (Map<String, Object>) registroResponse.get("data");
            
            //Crear la boleta
            Boleta boleta = new Boleta();
            boleta.setUsuarioId(usuarioId);
            boleta.setVentaId(request.getVentaId());
            boleta.setTotal(total);
            boleta.setFecha(LocalDateTime.now());
            boleta.setDetalle(generarDetalle(ventaData, userData));
            
            Boleta boletaGuardada = boletaRepository.save(boleta);
            
            //Crear respuesta
            return crearBoletaResponse(boletaGuardada, userData, ventaData);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al generar boleta: " + e.getMessage());
        }
    }


        // Obtener boleta por ID con detalles de usuario y venta
    public BoletaResponse obtenerBoletaPorId(Long id) {
        Boleta boleta = boletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Boleta no encontrada"));
        
        try {
            //Obtener datos del usuario
            //Se mapea a registroServiceUrl para obtener info del usuario
            String registroUrl = registroServiceUrl + "/api/registro/" + boleta.getUsuarioId();
            Map<String, Object> registroResponse = restTemplate.getForObject(registroUrl, Map.class);
            Map<String, Object> userData = null;
            if (registroResponse != null && (Boolean) registroResponse.get("success")) {
                userData = (Map<String, Object>) registroResponse.get("data");
            }
            
            //Obtener datos de la venta
            String ventaUrl = ventasServiceUrl + "/api/ventas/" + boleta.getVentaId();
            Map<String, Object> ventaResponse = restTemplate.getForObject(ventaUrl, Map.class);
            Map<String, Object> ventaData = null;
            if (ventaResponse != null && (Boolean) ventaResponse.get("success")) {
                ventaData = (Map<String, Object>) ventaResponse.get("data");
            }
            
            return crearBoletaResponse(boleta, userData, ventaData);
            
        } catch (Exception e) {
            //Si falla la consulta, devolver solo datos de la boleta
            return crearBoletaResponseSimple(boleta);
        }
    }

    public List<BoletaResponse> obtenerBoletasPorUsuario(Long usuarioId) {
        List<Boleta> boletas = boletaRepository.findByUsuarioId(usuarioId);
        List<BoletaResponse> responses = new ArrayList<>();
        
        for (Boleta boleta : boletas) {
            try {
                responses.add(obtenerBoletaPorId(boleta.getId()));
            } catch (Exception e) {
                responses.add(crearBoletaResponseSimple(boleta));
            }
        }
        
        return responses;
    }

    public List<Boleta> listarTodas() {
        return boletaRepository.findAll();
    }

    private String generarDetalle(Map<String, Object> ventaData, Map<String, Object> userData) {
        StringBuilder detalle = new StringBuilder();
        detalle.append("=== BOLETA DE VENTA ===\n");
        detalle.append("Cliente: ").append(userData.get("nombre")).append("\n");
        detalle.append("Email: ").append(userData.get("email")).append("\n");
        detalle.append("Dirección: ").append(userData.get("direccion")).append("\n");
        detalle.append("Fecha: ").append(LocalDateTime.now()).append("\n");
        detalle.append("Venta ID: ").append(ventaData.get("id")).append("\n");
        detalle.append("Total: $").append(ventaData.get("total")).append("\n");
        detalle.append("========================");
        return detalle.toString();
    }

    private BoletaResponse crearBoletaResponse(Boleta boleta, Map<String, Object> userData, Map<String, Object> ventaData) {
        BoletaResponse response = new BoletaResponse();
        response.setId(boleta.getId());
        response.setTotal(boleta.getTotal());
        response.setFecha(boleta.getFecha());
        response.setDetalle(boleta.getDetalle());
        
        // Mapear datos de usuario
        if (userData != null) {
            UsuarioDTO usuario = new UsuarioDTO();
            usuario.setId(((Number) userData.get("id")).longValue());
            usuario.setNombre((String) userData.get("nombre"));
            usuario.setEmail((String) userData.get("email"));
            usuario.setDireccion((String) userData.get("direccion"));
            response.setUsuario(usuario);
        }
        
        // Mapear datos de venta
        if (ventaData != null) {
            VentaDTO venta = new VentaDTO();
            venta.setId(((Number) ventaData.get("id")).longValue());
            venta.setTotal(((Number) ventaData.get("total")).doubleValue());
            response.setVenta(venta);
        }
        
        return response;
    }

    private BoletaResponse crearBoletaResponseSimple(Boleta boleta) {
        BoletaResponse response = new BoletaResponse();
        response.setId(boleta.getId());
        response.setTotal(boleta.getTotal());
        response.setFecha(boleta.getFecha());
        response.setDetalle(boleta.getDetalle());
        return response;
    }
}
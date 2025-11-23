package com.apiVentas.ventas.service;

import com.apiVentas.ventas.model.Venta;
import com.apiVentas.ventas.model.DTO.ProductoDTO;
import com.apiVentas.ventas.model.DTO.UsuarioDTO;
import com.apiVentas.ventas.model.DTO.VentaRequest;
import com.apiVentas.ventas.model.DTO.VentaResponse;
import com.apiVentas.ventas.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${servicio.productos.url:http://localhost:8083}")
    private String productosServiceUrl;

    @Value("${servicio.registro.url:http://localhost:8084}")
    private String registroServiceUrl;

    @Value("${servicio.boleta.url:http://localhost:8088}")
    private String boletaServiceUrl;

    public VentaResponse crearVenta(VentaRequest request) {
        try {
            // 1. Validar que el usuario existe
            String usuarioUrl = registroServiceUrl + "/api/registro/" + request.getUsuarioId();
            Map<String, Object> usuarioResponse = restTemplate.getForObject(usuarioUrl, Map.class);
            
            if (usuarioResponse == null || !(Boolean) usuarioResponse.get("success")) {
                throw new RuntimeException("Usuario no encontrado");
            }

            // 2. Obtener información del producto
            String productoUrl = productosServiceUrl + "/api/productos/" + request.getProductoId();
            Map<String, Object> productoResponse = restTemplate.getForObject(productoUrl, Map.class);
            
            if (productoResponse == null || !(Boolean) productoResponse.get("success")) {
                throw new RuntimeException("Producto no encontrado");
            }

            Map<String, Object> productoData = (Map<String, Object>) productoResponse.get("data");
            Double precioUnitario = ((Number) productoData.get("precio")).doubleValue();
            Double total = precioUnitario * request.getCantidad();

            // 3. Crear la venta
            Venta venta = new Venta();
            venta.setUsuarioId(request.getUsuarioId());
            venta.setProductoId(request.getProductoId());
            venta.setCantidad(request.getCantidad());
            venta.setPrecioUnitario(precioUnitario);
            venta.setTotal(total);
            venta.setFecha(LocalDateTime.now());

            Venta ventaGuardada = ventaRepository.save(venta);

            // 4. Generar boleta automáticamente
            Long boletaId = generarBoleta(ventaGuardada.getId());
            
            // 5. Actualizar venta con el ID de la boleta
            if (boletaId != null) {
                ventaGuardada.setBoletaId(boletaId);
                ventaGuardada = ventaRepository.save(ventaGuardada);
            }

            // 6. Crear respuesta completa
            return crearVentaResponse(ventaGuardada, 
                (Map<String, Object>) usuarioResponse.get("data"), 
                productoData);

        } catch (Exception e) {
            throw new RuntimeException("Error al crear venta: " + e.getMessage());
        }
    }

    public VentaResponse obtenerVentaPorId(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        try {
            // Obtener datos del usuario
            String usuarioUrl = registroServiceUrl + "/api/registro/" + venta.getUsuarioId();
            Map<String, Object> usuarioResponse = restTemplate.getForObject(usuarioUrl, Map.class);
            Map<String, Object> usuarioData = null;
            if (usuarioResponse != null && (Boolean) usuarioResponse.get("success")) {
                usuarioData = (Map<String, Object>) usuarioResponse.get("data");
            }

            // Obtener datos del producto
            String productoUrl = productosServiceUrl + "/api/productos/" + venta.getProductoId();
            Map<String, Object> productoResponse = restTemplate.getForObject(productoUrl, Map.class);
            Map<String, Object> productoData = null;
            if (productoResponse != null && (Boolean) productoResponse.get("success")) {
                productoData = (Map<String, Object>) productoResponse.get("data");
            }

            return crearVentaResponse(venta, usuarioData, productoData);

        } catch (Exception e) {
            return crearVentaResponseSimple(venta);
        }
    }

    public List<VentaResponse> obtenerVentasPorUsuario(Long usuarioId) {
        List<Venta> ventas = ventaRepository.findByUsuarioId(usuarioId);
        List<VentaResponse> responses = new ArrayList<>();

        for (Venta venta : ventas) {
            try {
                responses.add(obtenerVentaPorId(venta.getId()));
            } catch (Exception e) {
                responses.add(crearVentaResponseSimple(venta));
            }
        }

        return responses;
    }

    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }

    public void eliminarVenta(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        ventaRepository.delete(venta);
    }

    private Long generarBoleta(Long ventaId) {
        try {
            String boletaUrl = boletaServiceUrl + "/api/boleta/generar";
            
            Map<String, Object> boletaRequest = new HashMap<>();
            boletaRequest.put("ventaId", ventaId);
            
            Map<String, Object> boletaResponse = restTemplate.postForObject(
                boletaUrl, 
                boletaRequest, 
                Map.class
            );
            
            if (boletaResponse != null && (Boolean) boletaResponse.get("success")) {
                Map<String, Object> boletaData = (Map<String, Object>) boletaResponse.get("data");
                Long boletaId = ((Number) boletaData.get("id")).longValue();
                System.out.println("Boleta generada automáticamente con ID: " + boletaId);
                return boletaId;
            }
            
            System.err.println("No se pudo generar la boleta automáticamente");
            return null;
            
        } catch (Exception e) {
            System.err.println("Error al generar boleta: " + e.getMessage());
            return null;
        }
    }

    private VentaResponse crearVentaResponse(Venta venta, Map<String, Object> usuarioData, Map<String, Object> productoData) {
        VentaResponse response = new VentaResponse();
        response.setId(venta.getId());
        response.setUsuarioId(venta.getUsuarioId());
        response.setProductoId(venta.getProductoId());
        response.setCantidad(venta.getCantidad());
        response.setPrecioUnitario(venta.getPrecioUnitario());
        response.setTotal(venta.getTotal());
        response.setFecha(venta.getFecha());
        response.setBoletaId(venta.getBoletaId());
        response.setEstado(venta.getEstado());

        if (usuarioData != null) {
            UsuarioDTO usuario = new UsuarioDTO();
            usuario.setId(((Number) usuarioData.get("id")).longValue());
            usuario.setNombre((String) usuarioData.get("nombre"));
            usuario.setEmail((String) usuarioData.get("email"));
            usuario.setDireccion((String) usuarioData.get("direccion"));
            response.setUsuario(usuario);
        }

        if (productoData != null) {
            ProductoDTO producto = new ProductoDTO();
            producto.setId(((Number) productoData.get("id")).longValue());
            producto.setNombre((String) productoData.get("nombre"));
            producto.setPrecio(((Number) productoData.get("precio")).doubleValue());
            producto.setDescripcion((String) productoData.get("descripcion"));
            response.setProducto(producto);
        }

        return response;
    }

    private VentaResponse crearVentaResponseSimple(Venta venta) {
        VentaResponse response = new VentaResponse();
        response.setId(venta.getId());
        response.setUsuarioId(venta.getUsuarioId());
        response.setProductoId(venta.getProductoId());
        response.setCantidad(venta.getCantidad());
        response.setPrecioUnitario(venta.getPrecioUnitario());
        response.setTotal(venta.getTotal());
        response.setFecha(venta.getFecha());
        response.setBoletaId(venta.getBoletaId());
        response.setEstado(venta.getEstado());
        return response;
    }
}
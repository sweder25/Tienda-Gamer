package com.apiVentas.ventas.service;

import com.apiVentas.ventas.model.Venta;
import com.apiVentas.ventas.model.DetalleVenta;
import com.apiVentas.ventas.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String PRODUCTOS_URL = "http://localhost:8083/api/productos";

    @Transactional
    public Venta crearVenta(Venta venta) {
        System.out.println("=== CREANDO VENTA EN SERVICE ===");
        
        venta.setFechaVenta(LocalDateTime.now());
        venta.setEstado("COMPLETADA");

        double total = 0;
        
        if (venta.getProductos() == null || venta.getProductos().isEmpty()) {
            throw new RuntimeException("La venta debe tener al menos un producto");
        }

        for (DetalleVenta detalle : venta.getProductos()) {
            String url = PRODUCTOS_URL + "/" + detalle.getProductoId();
            
            try {
                System.out.println("Consultando producto ID: " + detalle.getProductoId());
                
                ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
                Map<String, Object> responseBody = response.getBody();
                
                if (responseBody != null && responseBody.get("data") != null) {
                    Map<String, Object> productoData = (Map<String, Object>) responseBody.get("data");
                    
                    Integer stock = productoData.get("stock") instanceof Number ? 
                        ((Number) productoData.get("stock")).intValue() : 0;
                        
                    if (stock < detalle.getCantidad()) {
                        throw new RuntimeException("Stock insuficiente para producto ID " + 
                            detalle.getProductoId());
                    }
                    
                    Double precio = null;
                    if (productoData.get("precio") instanceof Number) {
                        precio = ((Number) productoData.get("precio")).doubleValue();
                    }
                    
                    if (precio != null) {
                        detalle.setPrecioUnitario(precio);
                        detalle.setSubtotal(precio * detalle.getCantidad());
                        total += detalle.getSubtotal();
                        
                        System.out.println("Producto procesado: " + productoData.get("nombre") + 
                                         " | Precio: " + precio + 
                                         " | Cantidad: " + detalle.getCantidad());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al obtener producto " + detalle.getProductoId() + ": " + e.getMessage());
                throw new RuntimeException("Producto no disponible. ID: " + detalle.getProductoId());
            }
            
            detalle.setVenta(venta);
        }

        venta.setTotal(total);
        System.out.println("Total de la venta: $" + total);
        
        Venta ventaGuardada = ventaRepository.save(venta);
        ventaRepository.flush();
        
        System.out.println("Venta guardada con ID: " + ventaGuardada.getId());

        return ventaGuardada;
    }

    public Venta obtenerPorId(Long id) {
        return ventaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }

    public List<Venta> obtenerPorUsuarioId(Long usuarioId) {
        return ventaRepository.findByUsuarioId(usuarioId);
    }

    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }
}
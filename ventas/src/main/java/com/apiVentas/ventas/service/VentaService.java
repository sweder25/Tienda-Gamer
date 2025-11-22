package com.apiVentas.ventas.service;

import com.apiVentas.ventas.model.DTO.ProductoDTO;
import com.apiVentas.ventas.model.DTO.UsuarioDTO;
import com.apiVentas.ventas.model.DTO.VentaRequest;
import com.apiVentas.ventas.model.DTO.VentaResponse;
import com.apiVentas.ventas.model.Venta;
import com.apiVentas.ventas.repository.VentaRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaService {

    private final VentaRepository repository;
    private final RestTemplate restTemplate;
    
    @Value("${usuarios.service.url:http://localhost:8086}")
    private String usuariosServiceUrl;
    
    @Value("${productos.service.url:http://localhost:8083}")
    private String productosServiceUrl;

    public VentaService(VentaRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public VentaResponse crearVenta(VentaRequest ventaRequest) {
        try {
            // 1. Validar que el usuario existe
            String urlUsuario = usuariosServiceUrl + "/api/usuarios/" + ventaRequest.getUsuarioRut();
            UsuarioDTO usuario = restTemplate.getForObject(urlUsuario, UsuarioDTO.class);
            
            if (usuario == null) {
                throw new RuntimeException("Usuario no encontrado");
            }
            
            // 2. Validar que el producto existe
            String urlProducto = productosServiceUrl + "/api/producto/" + ventaRequest.getProductoId();
            ProductoDTO producto = restTemplate.getForObject(urlProducto, ProductoDTO.class);
            
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado");
            }
            
            // 3. Validar cantidad
            if (ventaRequest.getCantidad() <= 0) {
                throw new RuntimeException("La cantidad debe ser mayor a 0");
            }
            
            // 4. Crear la venta
            Venta venta = new Venta();
            venta.setUsuarioRut(ventaRequest.getUsuarioRut());
            venta.setProductoId(ventaRequest.getProductoId());
            venta.setCantidad(ventaRequest.getCantidad());
            venta.setPrecioUnitario(producto.getPrecio());
            
            Venta ventaGuardada = repository.save(venta);
            
            // 5. Crear respuesta con información completa
            VentaResponse response = new VentaResponse();
            response.setId(ventaGuardada.getId());
            response.setUsuarioRut(ventaGuardada.getUsuarioRut());
            response.setNombreUsuario(usuario.getNombre());
            response.setProductoId(ventaGuardada.getProductoId());
            response.setNombreProducto(producto.getNombre());
            response.setCantidad(ventaGuardada.getCantidad());
            response.setPrecioUnitario(ventaGuardada.getPrecioUnitario());
            response.setPrecioTotal(ventaGuardada.getPrecioTotal());
            response.setFechaVenta(ventaGuardada.getFechaVenta());
            response.setEstado(ventaGuardada.getEstado());
            
            System.out.println("Venta creada exitosamente: " + ventaGuardada.getId());
            return response;
            
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Usuario o producto no encontrado");
        } catch (Exception e) {
            System.err.println("Error al crear venta: " + e.getMessage());
            throw new RuntimeException("Error al procesar la venta: " + e.getMessage());
        }
    }

    public List<VentaResponse> listarVentas() {
        List<Venta> ventas = repository.findAll();
        return ventas.stream().map(this::convertirAVentaResponse).collect(Collectors.toList());
    }

    public VentaResponse obtenerVenta(Long id) {
        Venta venta = repository.findById(id).orElse(null);
        if (venta == null) {
            return null;
        }
        return convertirAVentaResponse(venta);
    }

    public List<VentaResponse> listarVentasPorUsuario(Long usuarioRut) {
        List<Venta> ventas = repository.findByUsuarioRut(usuarioRut);
        return ventas.stream().map(this::convertirAVentaResponse).collect(Collectors.toList());
    }

    public List<VentaResponse> listarVentasPorProducto(Long productoId) {
        List<Venta> ventas = repository.findByProductoId(productoId);
        return ventas.stream().map(this::convertirAVentaResponse).collect(Collectors.toList());
    }

    private VentaResponse convertirAVentaResponse(Venta venta) {
        VentaResponse response = new VentaResponse();
        response.setId(venta.getId());
        response.setUsuarioRut(venta.getUsuarioRut());
        response.setProductoId(venta.getProductoId());
        response.setCantidad(venta.getCantidad());
        response.setPrecioUnitario(venta.getPrecioUnitario());
        response.setPrecioTotal(venta.getPrecioTotal());
        response.setFechaVenta(venta.getFechaVenta());
        response.setEstado(venta.getEstado());
        
        // Obtener información adicional si es necesario
        try {
            UsuarioDTO usuario = restTemplate.getForObject(
                usuariosServiceUrl + "/api/usuarios/" + venta.getUsuarioRut(), 
                UsuarioDTO.class
            );
            if (usuario != null) {
                response.setNombreUsuario(usuario.getNombre());
            }
            
            ProductoDTO producto = restTemplate.getForObject(
                productosServiceUrl + "/api/producto/" + venta.getProductoId(), 
                ProductoDTO.class
            );
            if (producto != null) {
                response.setNombreProducto(producto.getNombre());
            }
        } catch (Exception e) {
            System.err.println("Error al obtener información adicional: " + e.getMessage());
        }
        
        return response;
    }
}
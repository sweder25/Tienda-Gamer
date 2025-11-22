package com.apiBoleta.boleta.service;

import com.apiBoleta.boleta.model.DTO.BoletaRequest;
import com.apiBoleta.boleta.model.DTO.BoletaResponse;
import com.apiBoleta.boleta.model.DTO.UsuarioDTO;
import com.apiBoleta.boleta.model.DTO.VentaDTO;
import com.apiBoleta.boleta.model.Boleta;
import com.apiBoleta.boleta.repository.BoletaRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoletaService {

    private final BoletaRepository repository;
    private final RestTemplate restTemplate;
    
    @Value("${ventas.service.url:http://localhost:8087}")
    private String ventasServiceUrl;
    
    @Value("${usuarios.service.url:http://localhost:8086}")
    private String usuariosServiceUrl;

    public BoletaService(BoletaRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public BoletaResponse generarBoleta(BoletaRequest request) {
        try {
            // 1. Verificar si ya existe una boleta para esta venta
            if (repository.findByVentaId(request.getVentaId()).isPresent()) {
                throw new RuntimeException("Ya existe una boleta para esta venta");
            }
            
            // 2. Obtener información de la venta
            String urlVenta = ventasServiceUrl + "/api/ventas/" + request.getVentaId();
            VentaDTO venta = restTemplate.getForObject(urlVenta, VentaDTO.class);
            
            if (venta == null) {
                throw new RuntimeException("Venta no encontrada");
            }
            
            // 3. Obtener información del usuario
            String urlUsuario = usuariosServiceUrl + "/api/usuarios/" + venta.getUsuarioRut();
            UsuarioDTO usuario = restTemplate.getForObject(urlUsuario, UsuarioDTO.class);
            
            if (usuario == null) {
                throw new RuntimeException("Usuario no encontrado");
            }
            
            // 4. Crear la boleta
            Boleta boleta = new Boleta();
            boleta.setVentaId(venta.getId());
            boleta.setUsuarioRut(venta.getUsuarioRut());
            boleta.setNombreUsuario(usuario.getNombre());
            boleta.setEmailUsuario(usuario.getEmail());
            boleta.setProductoId(venta.getProductoId());
            boleta.setNombreProducto(venta.getNombreProducto());
            boleta.setCantidad(venta.getCantidad());
            boleta.setPrecioUnitario(venta.getPrecioUnitario());
            
            Boleta boletaGuardada = repository.save(boleta);
            
            System.out.println("Boleta generada exitosamente: " + boletaGuardada.getNumeroBoleta());
            
            return convertirABoletaResponse(boletaGuardada);
            
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Venta o usuario no encontrado");
        } catch (Exception e) {
            System.err.println("Error al generar boleta: " + e.getMessage());
            throw new RuntimeException("Error al procesar la boleta: " + e.getMessage());
        }
    }

    public List<BoletaResponse> listarBoletas() {
        List<Boleta> boletas = repository.findAll();
        return boletas.stream()
                .map(this::convertirABoletaResponse)
                .collect(Collectors.toList());
    }

    public BoletaResponse obtenerBoleta(Long id) {
        Boleta boleta = repository.findById(id).orElse(null);
        if (boleta == null) {
            return null;
        }
        return convertirABoletaResponse(boleta);
    }

    public BoletaResponse obtenerBoletaPorVenta(Long ventaId) {
        Boleta boleta = repository.findByVentaId(ventaId).orElse(null);
        if (boleta == null) {
            return null;
        }
        return convertirABoletaResponse(boleta);
    }

    public BoletaResponse obtenerBoletaPorNumero(String numeroBoleta) {
        Boleta boleta = repository.findByNumeroBoleta(numeroBoleta).orElse(null);
        if (boleta == null) {
            return null;
        }
        return convertirABoletaResponse(boleta);
    }

    public List<BoletaResponse> listarBoletasPorUsuario(Long usuarioRut) {
        List<Boleta> boletas = repository.findByUsuarioRut(usuarioRut);
        return boletas.stream()
                .map(this::convertirABoletaResponse)
                .collect(Collectors.toList());
    }

    private BoletaResponse convertirABoletaResponse(Boleta boleta) {
        BoletaResponse response = new BoletaResponse();
        response.setId(boleta.getId());
        response.setNumeroBoleta(boleta.getNumeroBoleta());
        response.setVentaId(boleta.getVentaId());
        response.setUsuarioRut(boleta.getUsuarioRut());
        response.setNombreUsuario(boleta.getNombreUsuario());
        response.setEmailUsuario(boleta.getEmailUsuario());
        response.setProductoId(boleta.getProductoId());
        response.setNombreProducto(boleta.getNombreProducto());
        response.setCantidad(boleta.getCantidad());
        response.setPrecioUnitario(boleta.getPrecioUnitario());
        response.setSubtotal(boleta.getSubtotal());
        response.setIva(boleta.getIva());
        response.setTotal(boleta.getTotal());
        response.setFechaEmision(boleta.getFechaEmision());
        return response;
    }
}
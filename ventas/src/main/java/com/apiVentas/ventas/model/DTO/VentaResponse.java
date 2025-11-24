package com.apiVentas.ventas.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaResponse {
    private Long id;
    private Long usuarioId;
    private Double total;
    private LocalDateTime fecha;
    private String estado;
    
    // Datos de facturación
    private String nombreCliente;
    private String apellidoCliente;
    private String emailCliente;
    private String direccion;
    private String region;
    private String codigoPostal;
    private String metodoPago;
    
    // Datos del usuario (opcional)
    private UsuarioDTO usuario;
    
    // Lista de productos
    private List<DetalleVentaDTO> productos;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetalleVentaDTO {
        private Long productoId;
        private String nombreProducto;
        private Integer cantidad;
        private Double precioUnitario;
        private Double subtotal;
    }
}
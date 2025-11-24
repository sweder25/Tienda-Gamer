package com.apiVentas.ventas.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaRequest {
    private Long usuarioId;
    private List<ProductoCarrito> productos;
    
    // Datos de facturación
    private String nombreCliente;
    private String apellidoCliente;
    private String emailCliente;
    private String direccion;
    private String region;
    private String codigoPostal;
    private String metodoPago;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductoCarrito {
        private Long productoId;
        private Integer cantidad;
    }
}
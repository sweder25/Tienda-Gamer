package com.apiBoleta.boleta.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoletaResponse {
    private Long id;
    private String numeroBoleta;
    private Long ventaId;
    
    // Datos del cliente
    private Long usuarioRut;
    private String nombreUsuario;
    private String emailUsuario;
    
    // Datos del producto
    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    
    // Totales
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal total;
    
    private LocalDateTime fechaEmision;
}
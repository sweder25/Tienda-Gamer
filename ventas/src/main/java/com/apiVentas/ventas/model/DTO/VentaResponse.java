package com.apiVentas.ventas.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaResponse {
    private Long id;
    private Long usuarioId;
    private Long productoId;
    private Integer cantidad;
    private Double precioUnitario;
    private Double total;
    private LocalDateTime fecha;
    private Long boletaId;
    private String estado;
    private UsuarioDTO usuario;
    private ProductoDTO producto;
}
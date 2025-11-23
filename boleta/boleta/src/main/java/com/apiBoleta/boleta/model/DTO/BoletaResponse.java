package com.apiBoleta.boleta.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoletaResponse {
    private Long id;
    private Double total;
    private LocalDateTime fecha;
    private String detalle;
    private UsuarioDTO usuario;
    private VentaDTO venta;
}
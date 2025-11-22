package com.apiVentas.ventas.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaRequest {
    private Long usuarioRut;
    private Long productoId;
    private Integer cantidad;
}
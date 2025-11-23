package com.apiVentas.ventas.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaRequest {
    private Long usuarioId;
    private Long productoId;
    private Integer cantidad;
}
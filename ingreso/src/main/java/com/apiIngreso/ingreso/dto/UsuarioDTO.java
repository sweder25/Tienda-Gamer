package com.apiIngreso.ingreso.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long rut;
    private String nombre;
    private String email;
    private String rol;
}
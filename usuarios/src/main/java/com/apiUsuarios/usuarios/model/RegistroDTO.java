package com.apiUsuarios.usuarios.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDTO {
    private Long id;
    private Long usuarioId;
    private LocalDateTime fechaRegistro;
}
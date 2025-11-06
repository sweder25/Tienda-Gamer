
package com.apiUsuarios.usuarios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    private Long rut;
    
    @Column(name = "dv")
    private String dv;
    
    @Column(name = "nombre")
    private String nombre;
    
    @Column(name = "email")
    private String email;

    @Column(name = "direccion")
    private String direccion;
    
    @Column(name = "contrasena")
    private String contrasena;
    
}
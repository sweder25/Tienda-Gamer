
package com.apiRegistro.registro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long rut;

    @Column(name = "nombre", nullable = false) 
    private String nombre;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "contrasena", nullable = false)
    private String contrasena;
    
}
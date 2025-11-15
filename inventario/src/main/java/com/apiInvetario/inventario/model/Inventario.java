package com.apiInventario.inventario.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia al producto por su ID del microservicio de productos
    @Column(name="producto_id", nullable = false)
    private Long productoId;

    @Column(name="cantidad_disponible", nullable = false)
    private Integer cantidadDisponible;
}

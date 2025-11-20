package com.apiProductos.productos.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Entity
@Table(name="Productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Productos{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nombre_producto", nullable = false)
    private String nombreProducto;

    @Column(name="descripcion", nullable = false)
    private String descripcion;
}
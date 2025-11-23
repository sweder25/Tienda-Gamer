package com.apiVentas.ventas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long usuarioId;
    
    @Column(nullable = false)
    private Long productoId;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(nullable = false)
    private Double precioUnitario;
    
    @Column(nullable = false, name = "precio_total")
    private Double total;
    
    @Column(nullable = false, name = "fecha_venta")
    private LocalDateTime fecha;
    
    private Long boletaId;
    
    @Column(nullable = false)
    private String estado = "COMPLETADA";


}
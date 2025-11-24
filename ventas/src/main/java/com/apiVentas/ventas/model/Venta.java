package com.apiVentas.ventas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private Double total;
    
    @Column(nullable = false)
    private LocalDateTime fechaVenta;  // CAMBIADO DE 'fecha' A 'fechaVenta'
    
    @Column(nullable = false)
    private String estado;
    
    @Column(nullable = false)
    private String nombreCliente;
    
    @Column
    private String apellidoCliente;
    
    @Column(nullable = false)
    private String emailCliente;
    
    @Column(nullable = false)
    private String direccion;
    
    @Column
    private String region;
    
    @Column
    private String codigoPostal;
    
    @Column(nullable = false)
    private String metodoPago;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "venta_id")
    private List<DetalleVenta> productos;
}
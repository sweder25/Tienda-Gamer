package com.apiVentas.ventas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
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

    @Column(name = "usuario_rut", nullable = false)
    private Long usuarioRut;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "precio_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTotal;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDateTime fechaVenta;

    @Column(name = "estado", nullable = false)
    private String estado; // PENDIENTE, COMPLETADA, CANCELADA

    @PrePersist
    protected void onCreate() {
        this.fechaVenta = LocalDateTime.now();
        this.precioTotal = this.precioUnitario.multiply(BigDecimal.valueOf(this.cantidad));
        if (this.estado == null) {
            this.estado = "COMPLETADA";
        }
    }
}
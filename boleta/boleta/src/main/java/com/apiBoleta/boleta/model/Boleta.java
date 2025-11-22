package com.apiBoleta.boleta.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "boletas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_boleta", nullable = false, unique = true)
    private String numeroBoleta;

    @Column(name = "venta_id", nullable = false, unique = true)
    private Long ventaId;

    @Column(name = "usuario_rut", nullable = false)
    private Long usuarioRut;

    @Column(name = "nombre_usuario", nullable = false)
    private String nombreUsuario;

    @Column(name = "email_usuario", nullable = false)
    private String emailUsuario;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "nombre_producto", nullable = false)
    private String nombreProducto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "iva", nullable = false, precision = 10, scale = 2)
    private BigDecimal iva;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @PrePersist
    protected void onCreate() {
        this.fechaEmision = LocalDateTime.now();
        this.numeroBoleta = generarNumeroBoleta();
        calcularTotales();
    }

    private String generarNumeroBoleta() {
        // Formato: BOL-YYYYMMDD-HHMMSS-ID
        LocalDateTime now = LocalDateTime.now();
        return String.format("BOL-%04d%02d%02d-%02d%02d%02d",
                now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
                now.getHour(), now.getMinute(), now.getSecond());
    }

    private void calcularTotales() {
        // Calcular subtotal
        this.subtotal = this.precioUnitario.multiply(BigDecimal.valueOf(this.cantidad));
        
        // Calcular IVA (19% en Chile)
        this.iva = this.subtotal.multiply(BigDecimal.valueOf(0.19));
        
        // Calcular total
        this.total = this.subtotal.add(this.iva);
    }
}
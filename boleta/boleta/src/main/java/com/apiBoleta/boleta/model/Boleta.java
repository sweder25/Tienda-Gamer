package com.apiBoleta.boleta.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "boletas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_boleta", nullable = false, unique = true)
    private String numero;

    @Column(name = "venta_id", nullable = false)
    private Long ventaId;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "email_cliente", nullable = false)
    private String emailCliente;

    @Column(name = "direccion_envio")
    private String direccionEnvio;

    @Column(name = "metodo_pago")
    private String metodoPago;

    @Column(length = 1000)
    private String estado;

}
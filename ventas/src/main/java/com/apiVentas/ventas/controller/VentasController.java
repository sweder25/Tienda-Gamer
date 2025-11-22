package com.apiVentas.ventas.controller;

import com.apiVentas.ventas.model.DTO.VentaRequest;
import com.apiVentas.ventas.model.DTO.VentaResponse;
import com.apiVentas.ventas.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@Tag(name = "Ventas", description = "Operaciones para gestionar ventas del sistema")
@CrossOrigin(origins = "*")
public class VentasController {

    private final VentaService ventaService;

    public VentasController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @Operation(summary = "Crear una nueva venta")
    @PostMapping
    public ResponseEntity<VentaResponse> crearVenta(@RequestBody VentaRequest ventaRequest) {
        try {
            VentaResponse venta = ventaService.crearVenta(ventaRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(venta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Listar todas las ventas")
    @GetMapping
    public ResponseEntity<List<VentaResponse>> listarVentas() {
        try {
            List<VentaResponse> ventas = ventaService.listarVentas();
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener una venta por ID")
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> obtenerVenta(@PathVariable Long id) {
        try {
            VentaResponse venta = ventaService.obtenerVenta(id);
            if (venta != null) {
                return ResponseEntity.ok(venta);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Listar ventas por usuario")
    @GetMapping("/usuario/{usuarioRut}")
    public ResponseEntity<List<VentaResponse>> listarVentasPorUsuario(@PathVariable Long usuarioRut) {
        try {
            List<VentaResponse> ventas = ventaService.listarVentasPorUsuario(usuarioRut);
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Listar ventas por producto")
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<VentaResponse>> listarVentasPorProducto(@PathVariable Long productoId) {
        try {
            List<VentaResponse> ventas = ventaService.listarVentasPorProducto(productoId);
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
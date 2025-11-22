package com.apiBoleta.boleta.controller;

import com.apiBoleta.boleta.model.DTO.BoletaRequest;
import com.apiBoleta.boleta.model.DTO.BoletaResponse;
import com.apiBoleta.boleta.service.BoletaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boleta")
@Tag(name = "Boletas", description = "Operaciones para gestionar boletas del sistema")
@CrossOrigin(origins = "*")
public class BoletaController {

    private final BoletaService boletaService;

    public BoletaController(BoletaService boletaService) {
        this.boletaService = boletaService;
    }

    @Operation(summary = "Generar una boleta desde una venta")
    @PostMapping
    public ResponseEntity<BoletaResponse> generarBoleta(@RequestBody BoletaRequest request) {
        try {
            BoletaResponse boleta = boletaService.generarBoleta(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(boleta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Listar todas las boletas")
    @GetMapping
    public ResponseEntity<List<BoletaResponse>> listarBoletas() {
        try {
            List<BoletaResponse> boletas = boletaService.listarBoletas();
            return ResponseEntity.ok(boletas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener una boleta por ID")
    @GetMapping("/{id}")
    public ResponseEntity<BoletaResponse> obtenerBoleta(@PathVariable Long id) {
        try {
            BoletaResponse boleta = boletaService.obtenerBoleta(id);
            if (boleta != null) {
                return ResponseEntity.ok(boleta);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener boleta por número de boleta")
    @GetMapping("/numero/{numeroBoleta}")
    public ResponseEntity<BoletaResponse> obtenerBoletaPorNumero(@PathVariable String numeroBoleta) {
        try {
            BoletaResponse boleta = boletaService.obtenerBoletaPorNumero(numeroBoleta);
            if (boleta != null) {
                return ResponseEntity.ok(boleta);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener boleta por ID de venta")
    @GetMapping("/venta/{ventaId}")
    public ResponseEntity<BoletaResponse> obtenerBoletaPorVenta(@PathVariable Long ventaId) {
        try {
            BoletaResponse boleta = boletaService.obtenerBoletaPorVenta(ventaId);
            if (boleta != null) {
                return ResponseEntity.ok(boleta);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Listar boletas por usuario")
    @GetMapping("/usuario/{usuarioRut}")
    public ResponseEntity<List<BoletaResponse>> listarBoletasPorUsuario(@PathVariable Long usuarioRut) {
        try {
            List<BoletaResponse> boletas = boletaService.listarBoletasPorUsuario(usuarioRut);
            return ResponseEntity.ok(boletas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
package com.apiRegistro.registro.controller;

import com.apiRegistro.registro.model.Registro;
import com.apiRegistro.registro.service.RegistroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registro")
@Tag(name = "Registros", description = "Operaciones para gestionar registros de usuarios del sistema")
public class RegistroController {

    private final RegistroService service;

    public RegistroController(RegistroService service) {
        this.service = service;
    }

    @Operation(summary = "Crear un nuevo registro")
    @PostMapping
    public ResponseEntity<Registro> crearRegistro(@RequestBody Registro registro) {
        try {
            Registro registroCreado = service.crearRegistro(registro);
            return ResponseEntity.status(HttpStatus.CREATED).body(registroCreado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Listar todos los registros")
    @GetMapping
    public ResponseEntity<List<Registro>> listarRegistros() {
        try {
            List<Registro> registros = service.listarRegistros();
            return ResponseEntity.ok(registros);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener un registro por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Registro> obtenerRegistro(@PathVariable Long id) {
        try {
            Registro registro = service.obtenerRegistro(id);
            if (registro != null) {
                return ResponseEntity.ok(registro);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener registro por ID de usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Registro> obtenerPorUsuarioId(@PathVariable Long usuarioId) {
        try {
            Registro registro = service.obtenerPorUsuarioId(usuarioId);
            if (registro != null) {
                return ResponseEntity.ok(registro);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Eliminar un registro")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRegistro(@PathVariable Long id) {
        try {
            Registro registro = service.obtenerRegistro(id);
            if (registro == null) {
                return ResponseEntity.notFound().build();
            }
            service.eliminarRegistro(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
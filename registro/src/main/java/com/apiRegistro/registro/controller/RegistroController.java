package com.apiRegistro.registro.controller;

import com.apiRegistro.registro.model.Registro;
import com.apiRegistro.registro.model.UsuarioRequest;
import com.apiRegistro.registro.service.RegistroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registro")
@Tag(name = "Registro", description = "API para gestión de registros de usuarios")
@CrossOrigin(origins = "*")
public class RegistroController {

    @Autowired
    private RegistroService registroService;

    @PostMapping("/registrar")
    @Operation(summary = "Registrar nuevo usuario")
    public ResponseEntity<Map<String, Object>> registrarUsuario(@RequestBody UsuarioRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Registro registro = registroService.registrarUsuario(request);
            response.put("success", true);
            response.put("message", "Usuario registrado exitosamente");
            response.put("data", registro);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener registro por ID")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Registro registro = registroService.obtenerPorId(id);
            response.put("success", true);
            response.put("data", registro);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Obtener registro por email")
    public ResponseEntity<Map<String, Object>> obtenerPorEmail(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            Registro registro = registroService.obtenerPorEmail(email);
            response.put("success", true);
            response.put("data", registro);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todos los registros")
    public ResponseEntity<Map<String, Object>> listarTodos() {
        Map<String, Object> response = new HashMap<>();
        List<Registro> registros = registroService.listarTodos();
        response.put("success", true);
        response.put("data", registros);
        response.put("total", registros.size());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/actualizar/{id}")
    @Operation(summary = "Actualizar registro")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id, @RequestBody UsuarioRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Registro registro = registroService.actualizar(id, request);
            response.put("success", true);
            response.put("message", "Registro actualizado exitosamente");
            response.put("data", registro);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar registro")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            registroService.eliminar(id);
            response.put("success", true);
            response.put("message", "Registro eliminado exitosamente");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
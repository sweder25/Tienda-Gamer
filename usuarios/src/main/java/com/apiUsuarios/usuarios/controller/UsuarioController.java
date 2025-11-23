package com.apiUsuarios.usuarios.controller;

import com.apiUsuarios.usuarios.model.Usuario;
import com.apiUsuarios.usuarios.model.UsuarioRequest;
import com.apiUsuarios.usuarios.service.UsuarioService;
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
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "API para gestión de usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/crear")
    @Operation(summary = "Crear nuevo usuario")
    public ResponseEntity<Map<String, Object>> crearUsuario(@RequestBody UsuarioRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Usuario usuario = usuarioService.crearUsuario(request);
            response.put("success", true);
            response.put("message", "Usuario creado exitosamente");
            response.put("data", usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Usuario usuario = usuarioService.obtenerPorId(id);
            response.put("success", true);
            response.put("data", usuario);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Obtener usuario por email")
    public ResponseEntity<Map<String, Object>> obtenerPorEmail(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            Usuario usuario = usuarioService.obtenerPorEmail(email);
            response.put("success", true);
            response.put("data", usuario);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<Map<String, Object>> listarTodos() {
        Map<String, Object> response = new HashMap<>();
        List<Usuario> usuarios = usuarioService.listarTodos();
        response.put("success", true);
        response.put("data", usuarios);
        response.put("total", usuarios.size());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/actualizar/{id}")
    @Operation(summary = "Actualizar usuario")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id, @RequestBody UsuarioRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Usuario usuario = usuarioService.actualizar(id, request);
            response.put("success", true);
            response.put("message", "Usuario actualizado exitosamente");
            response.put("data", usuario);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar usuario")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            usuarioService.eliminar(id);
            response.put("success", true);
            response.put("message", "Usuario eliminado exitosamente");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
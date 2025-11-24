package com.apiUsuarios.usuarios.controller;

import com.apiUsuarios.usuarios.model.Usuario;
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

    @PostMapping("/login")
    @Operation(summary = "Validar credenciales de usuario")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credenciales) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== UsuarioController.login ===");
            System.out.println("Request body: " + credenciales);
            
            String email = credenciales.get("email");
            String password = credenciales.get("password");
            
            System.out.println("Email recibido: " + email);
            System.out.println("Password recibido: " + (password != null ? "****" : "null"));
            
            if (email == null || email.isEmpty()) {
                response.put("success", false);
                response.put("message", "Email es requerido");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (password == null || password.isEmpty()) {
                response.put("success", false);
                response.put("message", "Password es requerido");
                return ResponseEntity.badRequest().body(response);
            }

            boolean valido = usuarioService.validarCredenciales(email, password);
            System.out.println("Resultado validación: " + valido);

            if (valido) {
                response.put("success", true);
                response.put("message", "Credenciales válidas");
                System.out.println("Login exitoso para: " + email);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Credenciales inválidas");
                System.out.println("Login fallido para: " + email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (Exception e) {
            System.err.println("ERROR en login: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al validar credenciales: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/crear")
    @Operation(summary = "Crear nuevo usuario")
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Usuario usuario) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("Creando usuario: " + usuario.getEmail());
            Usuario nuevoUsuario = usuarioService.crear(usuario);
            response.put("success", true);
            response.put("message", "Usuario creado exitosamente");
            response.put("data", nuevoUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
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

}
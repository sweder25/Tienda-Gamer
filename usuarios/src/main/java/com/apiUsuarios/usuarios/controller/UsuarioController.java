package com.apiUsuarios.usuarios.controller;

import com.apiUsuarios.usuarios.model.Usuario;
import com.apiUsuarios.usuarios.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones para gestionar usuarios del sistema")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    // === OBTENER TODOS LOS USUARIOS ===
    @Operation(summary = "Obtener todos los usuarios")
    @Description("Este endpoint permite obtener una lista de todos los usuarios registrados en el sistema.")
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listarUsuarios();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            logger.error("Error al obtener todos los usuarios: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // === OBTENER USUARIO POR RUT ===
    @Operation(summary = "Obtener usuario por RUT")
    @Description("Este endpoint permite obtener un usuario específico utilizando su RUT.")
    @GetMapping("/{rut}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long rut) {
        try {
            Usuario usuario = usuarioService.obtenerUsuario(rut);
            if (usuario != null) {
                return ResponseEntity.ok(usuario);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error interno al obtener usuario {}: {}", rut, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // === CREAR USUARIO ===
    @Operation(summary = "Crear un nuevo usuario")
    @Description("Este endpoint permite crear un nuevo usuario en el sistema y registrarlo automáticamente.")
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        try {
            // Verificar si el RUT ya existe
            Usuario usuarioExistente = usuarioService.obtenerUsuario(usuario.getRut());
            if (usuarioExistente != null) {
                logger.warn("Intento de crear usuario con RUT duplicado: {}", usuario.getRut());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            
            Usuario usuarioCreado = usuarioService.crearUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
            
        } catch (Exception e) {
            logger.error("Error interno al crear usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // === ELIMINAR USUARIO ===
    @Operation(summary = "Eliminar un usuario existente")
    @Description("Este endpoint permite eliminar un usuario del sistema utilizando su RUT.")
    @DeleteMapping("/{rut}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long rut) {
        try {
            Usuario usuario = usuarioService.obtenerUsuario(rut);
            if (usuario == null) {
                return ResponseEntity.notFound().build();
            }
            
            usuarioService.eliminarUsuario(rut);
            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            logger.error("Error interno al eliminar usuario {}: {}", rut, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
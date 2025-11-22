package com.apiIngreso.ingreso.controller;

import com.apiIngreso.ingreso.dto.LoginRequest;
import com.apiIngreso.ingreso.dto.LoginResponse;
import com.apiIngreso.ingreso.service.IngresoService; // Crearemos este servicio

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingreso")
@Tag(name = "Ingreso", description = "Operaciones para gestionar el ingreso y autenticación de usuarios")
public class IngresoController {

    @Autowired
    private IngresoService ingresoService;

    @Operation(summary = "Login de usuario", description = "Este endpoint permite a un usuario autenticarse en el sistema.")
    @Description("El usuario debe proporcionar su email y contraseña para recibir un token de autenticación si las credenciales son correctas.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = ingresoService.validarLogin(request.getEmail(), request.getContrasena());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Si las credenciales son incorrectas, envía un error 401
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
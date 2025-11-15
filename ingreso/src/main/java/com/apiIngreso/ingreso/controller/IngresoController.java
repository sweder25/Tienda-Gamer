package com.apiIngreso.ingreso.controller;

import com.apiIngreso.ingreso.dto.LoginRequest;
import com.apiIngreso.ingreso.dto.LoginResponse;
import com.apiIngreso.ingreso.service.IngresoService; // Crearemos este servicio
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class IngresoController {

    @Autowired
    private IngresoService ingresoService;

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
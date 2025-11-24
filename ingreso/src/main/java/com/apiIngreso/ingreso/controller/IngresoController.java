package com.apiIngreso.ingreso.controller;

import com.apiIngreso.ingreso.dto.LoginRequest;
import com.apiIngreso.ingreso.dto.LoginResponse;
import com.apiIngreso.ingreso.service.IngresoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ingreso")
@Tag(name = "Ingreso", description = "API para autenticación de usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class IngresoController {

    @Autowired
    private IngresoService ingresoService;

    @PostMapping("/login")
    @Operation(summary = "Login de usuario")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("=== POST /api/ingreso/login ===");
            System.out.println("Email recibido: " + loginRequest.getEmail());
            
            LoginResponse response = ingresoService.login(loginRequest);
            
            System.out.println("Login exitoso para: " + response.getUsuario().getNombre());
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            System.err.println("Error en login: " + e.getMessage());
            
            LoginResponse errorResponse = new LoginResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage(e.getMessage());
            errorResponse.setUsuario(null);
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            
            LoginResponse errorResponse = new LoginResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Error interno del servidor");
            errorResponse.setUsuario(null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
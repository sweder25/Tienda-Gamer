package com.apiIngreso.ingreso.controller;

import com.apiIngreso.ingreso.dto.LoginRequest;
import com.apiIngreso.ingreso.dto.LoginResponse;
import com.apiIngreso.ingreso.service.IngresoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingreso")
@Tag(name = "Ingreso", description = "Operaciones de autenticación y login")
@CrossOrigin(origins = "*")
public class IngresoController {

    private final IngresoService ingresoService;

    public IngresoController(IngresoService ingresoService) {
        this.ingresoService = ingresoService;
    }

    @Operation(summary = "Iniciar sesión", description = "Permite a un usuario autenticarse en el sistema")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Validar que los campos no estén vacíos
            if (loginRequest.getRut() == null || loginRequest.getPassword() == null || 
                loginRequest.getPassword().isEmpty()) {
                LoginResponse response = new LoginResponse(false, "RUT y contraseña son requeridos", null);
                return ResponseEntity.badRequest().body(response);
            }
            
            LoginResponse response = ingresoService.login(loginRequest);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
        } catch (Exception e) {
            LoginResponse response = new LoginResponse(false, "Error interno del servidor", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Verificar estado del servicio")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Servicio de ingreso activo");
    }
}
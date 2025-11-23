package com.apiIngreso.ingreso.controller;

import com.apiIngreso.ingreso.dto.LoginRequest;
import com.apiIngreso.ingreso.dto.LoginResponse;
import com.apiIngreso.ingreso.service.IngresoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingreso")
@Tag(name = "Ingreso", description = "API para autenticación de usuarios")
//CrossOrigin permite solicitudes desde cualquier origen
//ejemplo: localhost, dominios específicos, etc.
@CrossOrigin(origins = "*")
public class IngresoController {

    @Autowired
    private IngresoService ingresoService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = ingresoService.login(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.status(401).body(response);
    }
}
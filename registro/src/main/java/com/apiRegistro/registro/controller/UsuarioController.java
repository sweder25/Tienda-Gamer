package com.apiRegistro.registro.controller; // O tu paquete

import com.apiRegistro.registro.model.Usuario;
import com.apiRegistro.registro.model.UsuarioRequest;
import com.apiRegistro.registro.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registro")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody UsuarioRequest request) {
        Usuario nuevoUsuario = usuarioService.registrarUsuario(request);
        return ResponseEntity.status(201).body(nuevoUsuario); // 201 Created
    }
}
package com.apiIngreso.ingreso.service;


import com.apiIngreso.ingreso.dto.LoginResponse;
import com.apiIngreso.ingreso.model.Usuario;
import com.apiIngreso.ingreso.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IngresoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ... (en la clase IngresoService)

public LoginResponse validarLogin(String email, String password) {
    // 1. Buscar al usuario por email
    Optional<Usuario> optUsuario = usuarioRepository.findByEmail(email);

    if (!optUsuario.isPresent()) {
        throw new RuntimeException("Credenciales inválidas: Usuario no encontrado");
    }

    Usuario usuario = optUsuario.get();

    // 2. CORRECCIÓN: Comparar con 'getContrasena()' en lugar de 'getPassword()'
    if (usuario.getContrasena().equals(password)) {
        // Éxito
        return new LoginResponse("Ingreso exitoso", usuario.getNombre());
    } else {
        // Fracaso
        throw new RuntimeException("Credenciales inválidas: Contraseña incorrecta");
    }
}


}
package com.apiRegistro.registro.service;


import com.apiRegistro.registro.model.Usuario;
import com.apiRegistro.registro.model.UsuarioRequest;
import com.apiRegistro.registro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario registrarUsuario(UsuarioRequest request) {
        
        Usuario nuevoUsuario = new Usuario();
        
        // --- LA SOLUCIÓN AL ERROR ---
        // Asignamos manualmente el ID (rut) antes de guardar
        nuevoUsuario.setRut(request.getRut()); 
        // -------------------------

        nuevoUsuario.setDv(request.getDv());
        nuevoUsuario.setNombre(request.getNombre());
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setDireccion(request.getDireccion());
        
        // Usamos el nombre de campo correcto de tu entidad
        nuevoUsuario.setContrasena(request.getContrasena()); 

        return usuarioRepository.save(nuevoUsuario);
    }
}
package com.apiUsuarios.usuarios.service;

import com.apiUsuarios.usuarios.model.Usuario;
import com.apiUsuarios.usuarios.model.UsuarioRequest;
import com.apiUsuarios.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario crearUsuario(UsuarioRequest request) {
        try {
            // Validar que el email no exista
            if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new RuntimeException("el email ya esta registrado imbecil");
            }

            Usuario usuario = new Usuario();
            usuario.setEmail(request.getEmail());
            usuario.setPassword(request.getPassword());

            return usuarioRepository.save(usuario);

        } catch (Exception e) {
            throw new RuntimeException("Error al crear usuario imbecil: " + e.getMessage());
        }
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado imbecil"));
    }

    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado imbecil"));
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario actualizar(Long id, UsuarioRequest request) {
        Usuario usuario = obtenerPorId(id);
        usuario.setEmail(request.getEmail());
        usuario.setPassword(request.getPassword());
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}
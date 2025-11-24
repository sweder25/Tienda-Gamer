package com.apiUsuarios.usuarios.service;

import com.apiUsuarios.usuarios.model.Usuario;
import com.apiUsuarios.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean validarCredenciales(String email, String password) {
        System.out.println("=== UsuarioService.validarCredenciales ===");
        System.out.println("Email: " + email);
        
        Usuario usuario = usuarioRepository.findByEmail(email);
        
        if (usuario == null) {
            System.out.println("Usuario no encontrado en BD");
            return false;
        }
        
        System.out.println("Usuario encontrado ID: " + usuario.getId());
        System.out.println("Comparando passwords...");
        boolean valido = usuario.getPassword().equals(password);
        System.out.println("Password válido: " + valido);
        
        return valido;
    }

    public Usuario crear(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            throw new RuntimeException("El email ya está registrado");
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario obtenerPorId(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            return usuario.get();
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    public Usuario obtenerPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        return usuario;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario actualizar(Long id, Usuario usuario) {
        Usuario existente = obtenerPorId(id);
        
        if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
            existente.setEmail(usuario.getEmail());
        }
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            existente.setPassword(usuario.getPassword());
        }
        
        return usuarioRepository.save(existente);
    }

    public void eliminar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuarioRepository.delete(usuario);
    }
}
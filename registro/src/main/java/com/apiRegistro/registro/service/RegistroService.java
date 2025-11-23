package com.apiRegistro.registro.service;

import com.apiRegistro.registro.model.Registro;
import com.apiRegistro.registro.model.UsuarioRequest;
import com.apiRegistro.registro.repository.RegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegistroService {

    @Autowired
    private RegistroRepository registroRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${servicio.usuarios.url:http://localhost:8086}")
    private String usuariosServiceUrl;

    public Registro registrarUsuario(UsuarioRequest request) {
    try {
        // 1. Validar que el email no exista
        if (registroRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        // 2. Crear registro
        Registro registro = new Registro();
        registro.setNombre(request.getNombre());
        registro.setEmail(request.getEmail());
        registro.setPassword(request.getPassword());
        registro.setDireccion(request.getDireccion());

        Registro registroGuardado = registroRepository.save(registro);

        // 3. Crear usuario en el servicio de USUARIOS (solo email y password)
        try {
            String usuariosUrl = usuariosServiceUrl + "/api/usuarios/crear";
            
            Map<String, Object> usuarioRequest = new HashMap<>();
            usuarioRequest.put("email", registroGuardado.getEmail());
            usuarioRequest.put("password", registroGuardado.getPassword());
            
            Map<String, Object> usuarioResponse = restTemplate.postForObject(
                usuariosUrl, 
                usuarioRequest, 
                Map.class
            );
            
            if (usuarioResponse != null && (Boolean) usuarioResponse.get("success")) {
                System.out.println("Usuario creado automáticamente en servicio USUARIOS");
            } else {
                System.err.println("Error al crear usuario en servicio USUARIOS");
            }
            
        } catch (Exception e) {
            System.err.println("Error al comunicarse con servicio USUARIOS: " + e.getMessage());
        }

        return registroGuardado;

    } catch (Exception e) {
        throw new RuntimeException("Error al registrar usuario: " + e.getMessage());
    }
}

    public Registro obtenerPorId(Long id) {
        return registroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
    }

    public Registro obtenerPorEmail(String email) {
        return registroRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public List<Registro> listarTodos() {
        return registroRepository.findAll();
    }

    public Registro actualizar(Long id, UsuarioRequest request) {
        Registro registro = obtenerPorId(id);
        registro.setNombre(request.getNombre());
        registro.setEmail(request.getEmail());
        registro.setPassword(request.getPassword());
        registro.setDireccion(request.getDireccion());
        return registroRepository.save(registro);
    }

    public void eliminar(Long id) {
        registroRepository.deleteById(id);
    }
}
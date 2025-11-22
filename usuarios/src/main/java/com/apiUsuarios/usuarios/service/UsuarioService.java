package com.apiUsuarios.usuarios.service;

import com.apiUsuarios.usuarios.model.RegistroDTO;
import com.apiUsuarios.usuarios.model.Usuario;
import com.apiUsuarios.usuarios.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final RestTemplate restTemplate;
    
    @Value("${registro.service.url:http://localhost:8084}")
    private String registroServiceUrl;

    public UsuarioService(UsuarioRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public Usuario crearUsuario(Usuario usuario) {
        // Guardar el usuario
        Usuario usuarioGuardado = repository.save(usuario);
        System.out.println("Usuario guardado con ID: " + usuarioGuardado.getRut());
        
        // Crear entrada en registro con el ID del usuario
        try {
            RegistroDTO registroDTO = new RegistroDTO();
            registroDTO.setUsuarioId(usuarioGuardado.getRut());
            
            String url = registroServiceUrl + "/api/registro";
            System.out.println("Enviando petición a: " + url);
            System.out.println("DTO: " + registroDTO);
            
            RegistroDTO respuesta = restTemplate.postForObject(url, registroDTO, RegistroDTO.class);
            System.out.println("Respuesta del registro: " + respuesta);
            
        } catch (Exception e) {
            System.err.println("Error al crear registro para usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return usuarioGuardado;
    }

    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    public Usuario obtenerUsuario(Long rut) {
        return repository.findById(rut).orElse(null);
    }

    public void eliminarUsuario(Long rut) {
        repository.deleteById(rut);
    }
}
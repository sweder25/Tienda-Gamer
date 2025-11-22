package com.apiIngreso.ingreso.service;

import com.apiIngreso.ingreso.dto.LoginRequest;
import com.apiIngreso.ingreso.dto.LoginResponse;
import com.apiIngreso.ingreso.dto.UsuarioDTO;
import com.apiIngreso.ingreso.model.Usuario;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class IngresoService {

    private final RestTemplate restTemplate;
    
    @Value("${usuarios.service.url:http://localhost:8086}")
    private String usuariosServiceUrl;

    public IngresoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            // Consultar usuario desde el servicio de usuarios
            String url = usuariosServiceUrl + "/api/usuarios/" + loginRequest.getRut();
            Usuario usuario = restTemplate.getForObject(url, Usuario.class);
            
            if (usuario == null) {
                return new LoginResponse(false, "Usuario no encontrado", null);
            }
            
            // Validar contraseña
            if (!usuario.getPassword().equals(loginRequest.getPassword())) {
                return new LoginResponse(false, "Contraseña incorrecta", null);
            }
            
            // Login exitoso - crear DTO sin password
            UsuarioDTO usuarioDTO = new UsuarioDTO(
                usuario.getRut(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol()
            );
            
            return new LoginResponse(true, "Login exitoso", usuarioDTO);
            
        } catch (HttpClientErrorException.NotFound e) {
            return new LoginResponse(false, "Usuario no encontrado", null);
        } catch (Exception e) {
            System.err.println("Error al intentar login: " + e.getMessage());
            return new LoginResponse(false, "Error al procesar el login", null);
        }
    }
}
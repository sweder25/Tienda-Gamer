package com.apiIngreso.ingreso.service;

import com.apiIngreso.ingreso.dto.LoginRequest;
import com.apiIngreso.ingreso.dto.LoginResponse;
import com.apiIngreso.ingreso.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Service
public class IngresoService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String USUARIOS_SERVICE_URL = "http://localhost:8086/api/usuarios";
    private static final String REGISTRO_SERVICE_URL = "http://localhost:8084/api/registro";

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            // Validar que los campos no estén vacíos
            if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
                throw new RuntimeException("El email es requerido");
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                throw new RuntimeException("La contraseña es requerida");
            }

            // PASO 1: Validar credenciales con el microservicio USUARIOS
            String urlUsuarios = USUARIOS_SERVICE_URL + "/login";
            
            // Crear objeto para enviar al microservicio de usuarios
            Map<String, String> credenciales = Map.of(
                "email", loginRequest.getEmail(),
                "password", loginRequest.getPassword()
            );
            
            ResponseEntity<Map> responseUsuarios;
            try {
                responseUsuarios = restTemplate.postForEntity(urlUsuarios, credenciales, Map.class);
            } catch (Exception e) {
                System.err.println("Error al validar con microservicio USUARIOS: " + e.getMessage());
                throw new RuntimeException("Credenciales inválidas");
            }
            
            if (responseUsuarios.getBody() == null || 
                !(boolean) responseUsuarios.getBody().getOrDefault("success", false)) {
                throw new RuntimeException("Credenciales inválidas");
            }

            // PASO 2: Obtener datos completos del usuario desde REGISTRO
            String urlRegistro = REGISTRO_SERVICE_URL + "/email/" + loginRequest.getEmail();
            
            ResponseEntity<Map> responseRegistro;
            try {
                responseRegistro = restTemplate.getForEntity(urlRegistro, Map.class);
            } catch (Exception e) {
                System.err.println("Error al obtener datos del microservicio REGISTRO: " + e.getMessage());
                throw new RuntimeException("Error al obtener datos del usuario");
            }
            
            if (responseRegistro.getBody() == null || 
                !(boolean) responseRegistro.getBody().get("success")) {
                throw new RuntimeException("No se encontraron datos del usuario");
            }

            Map<String, Object> data = (Map<String, Object>) responseRegistro.getBody().get("data");
            
            // PASO 3: Crear respuesta exitosa con datos completos
            UsuarioDTO usuario = new UsuarioDTO();
            usuario.setId(((Number) data.get("id")).longValue());
            usuario.setNombre((String) data.get("nombre"));
            usuario.setEmail((String) data.get("email"));
            usuario.setDireccion((String) data.get("direccion"));
            usuario.setRol((String) data.get("rol"));

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setSuccess(true);
            loginResponse.setMessage("Login exitoso");
            loginResponse.setUsuario(usuario);

            return loginResponse;

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Error inesperado en login: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al procesar el login. Intenta más tarde.");
        }
    }

    public boolean validarToken(String token) {
        return token != null && !token.isEmpty();
    }
}
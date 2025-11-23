package com.apiIngreso.ingreso.service;

import com.apiIngreso.ingreso.dto.LoginRequest;
import com.apiIngreso.ingreso.dto.LoginResponse;
import com.apiIngreso.ingreso.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class IngresoService {

    @Autowired
    private RestTemplate restTemplate;

    //Value sirve para inyectar valores desde application.properties o variables de entorno
    @Value("${servicio.usuarios.url:http://localhost:8086}")
    private String usuariosServiceUrl;

    @Value("${servicio.registro.url:http://localhost:8084}")
    private String registroServiceUrl;

    public LoginResponse login(LoginRequest request) {
        try {
            //Validar que el usuario existe en REGISTRO
            String registroUrl = registroServiceUrl + "/api/registro/email/" + request.getEmail();
            Map<String, Object> registroResponse = restTemplate.getForObject(registroUrl, Map.class);
            
            if (registroResponse == null || !(Boolean) registroResponse.get("success")) {
                LoginResponse response = new LoginResponse();
                response.setSuccess(false);
                response.setMessage("Usuario no encontrado");
                return response;
            }
            
            //Obtener datos del usuario desde REGISTRO
            Map<String, Object> userData = (Map<String, Object>) registroResponse.get("data");
            String passwordRegistro = (String) userData.get("password");
            
            //Validar contraseña
            if (!passwordRegistro.equals(request.getPassword())) {
                LoginResponse response = new LoginResponse();
                response.setSuccess(false);
                response.setMessage("Contraseña incorrecta");
                return response;
            }
            
            //Guardar/Actualizar credenciales en USUARIOS automáticamente
            guardarCredencialesEnUsuarios(request.getEmail(), request.getPassword());
            
            //Crear respuesta con datos completos
            UsuarioDTO usuario = new UsuarioDTO();
            usuario.setId(((Number) userData.get("id")).longValue());
            usuario.setNombre((String) userData.get("nombre"));
            usuario.setEmail((String) userData.get("email"));
            usuario.setDireccion((String) userData.get("direccion"));
            usuario.setRol((String) userData.get("rol"));
            
            LoginResponse response = new LoginResponse();
            response.setSuccess(true);
            response.setMessage("Login exitoso");
            response.setUsuario(usuario);
            return response;
            
        } catch (Exception e) {
            LoginResponse response = new LoginResponse();
            response.setSuccess(false);
            response.setMessage("Error en el servicio: " + e.getMessage());
            return response;
        }
    }
    
    private void guardarCredencialesEnUsuarios(String email, String password) {

        //Enviar solicitud a USUARIOS para guardar credenciales
        // Si falla, solo se registra el error en consola pero no afecta el login
        try {
            String sincronizarUrl = usuariosServiceUrl + "/api/usuarios/guardar-credenciales";
            
            Map<String, String> credenciales = new HashMap<>();
            credenciales.put("email", email);
            credenciales.put("password", password);
            
            restTemplate.postForObject(sincronizarUrl, credenciales, Map.class);
            
            System.out.println("Credenciales guardadas en USUARIOS para: " + email);
        } catch (Exception e) {
            System.err.println("Error al guardar credenciales en USUARIOS: " + e.getMessage());
        }
    }
}
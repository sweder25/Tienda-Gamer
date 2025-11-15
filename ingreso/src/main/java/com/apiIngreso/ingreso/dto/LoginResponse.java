package com.apiIngreso.ingreso.dto;

public class LoginResponse {
    private String message;
    private String nombreUsuario;

    public LoginResponse(String message, String nombreUsuario) {
        this.message = message;
        this.nombreUsuario = nombreUsuario;
    }

    // Getters y Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
}
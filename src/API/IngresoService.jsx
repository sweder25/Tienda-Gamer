import { authUtils } from '../utils/authUtils';

const API_URL = 'http://localhost:8080/api/auth';

export const ingresoService = {
  login: async (credenciales) => {
    try {
      console.log('=== Iniciando login con JWT ===');
      console.log('URL:', `${API_URL}/login`);
      console.log('Credenciales:', credenciales);
      
      const response = await fetch(`${API_URL}/login`, {
        method: 'POST',
        headers: { 
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(credenciales)
      });
      
      console.log('Response status:', response.status);
      
      if (!response.ok) {
        let errorMessage = 'Error al iniciar sesión';
        try {
          const errorData = await response.json();
          console.error('Error data:', errorData);
          errorMessage = errorData.message || errorData.error || errorMessage;
        } catch (e) {
          errorMessage = `Error ${response.status}: ${response.statusText}`;
        }
        throw new Error(errorMessage);
      }
      
      const data = await response.json();
      console.log('Login exitoso:', data);
      
      // Guardar token JWT y datos del usuario
      if (data.token) {
        authUtils.setToken(data.token);
        console.log('Token JWT guardado');
      }
      
      if (data.usuario) {
        authUtils.setUser(data.usuario);
        console.log('Usuario guardado:', data.usuario);
      }
      
      return data;
    } catch (error) {
      console.error('Error en login:', error);
      throw error;
    }
  },

  register: async (userData) => {
    try {
      console.log('=== Registrando usuario con JWT ===');
      console.log('URL:', `${API_URL}/register`);
      
      const response = await fetch(`${API_URL}/register`, {
        method: 'POST',
        headers: { 
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData)
      });
      
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Error al registrar usuario');
      }
      
      const data = await response.json();
      console.log('Registro exitoso:', data);
      
      // Guardar token JWT y datos del usuario si vienen en la respuesta
      if (data.token) {
        authUtils.setToken(data.token);
        console.log('Token JWT guardado');
      }
      
      if (data.usuario) {
        authUtils.setUser(data.usuario);
        console.log('Usuario guardado:', data.usuario);
      }
      
      return data;
    } catch (error) {
      console.error('Error en registro:', error);
      throw error;
    }
  },

  logout: () => {
    authUtils.clearAuth();
    console.log('Sesión cerrada - Token y usuario eliminados');
  },

  obtenerUsuarioActual: () => {
    return authUtils.getUser();
  },

  estaAutenticado: () => {
    return authUtils.isAuthenticated();
  },

  validarToken: async () => {
    try {
      const token = authUtils.getToken();
      if (!token) return false;

      const response = await fetch(`${API_URL}/validate`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      return response.ok;
    } catch (error) {
      console.error('Error al validar token:', error);
      return false;
    }
  }
};
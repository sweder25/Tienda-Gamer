// Utilidades para manejo de JWT
const TOKEN_KEY = 'jwt_token';
const USER_KEY = 'usuario';

export const authUtils = {
  // Guardar token
  setToken: (token) => {
    if (token) {
      localStorage.setItem(TOKEN_KEY, token);
    }
  },

  // Obtener token
  getToken: () => {
    return localStorage.getItem(TOKEN_KEY);
  },

  // Eliminar token
  removeToken: () => {
    localStorage.removeItem(TOKEN_KEY);
  },

  // Verificar si hay token
  hasToken: () => {
    return !!localStorage.getItem(TOKEN_KEY);
  },

  // Guardar información del usuario
  setUser: (user) => {
    if (user) {
      localStorage.setItem(USER_KEY, JSON.stringify(user));
    }
  },

  // Obtener información del usuario
  getUser: () => {
    const userStr = localStorage.getItem(USER_KEY);
    return userStr ? JSON.parse(userStr) : null;
  },

  // Eliminar información del usuario
  removeUser: () => {
    localStorage.removeItem(USER_KEY);
  },

  // Limpiar todo (logout completo)
  clearAuth: () => {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  },

  // Obtener headers con autorización
  getAuthHeaders: () => {
    const token = authUtils.getToken();
    const headers = {
      'Content-Type': 'application/json',
    };
    
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
    
    return headers;
  },

  // Verificar si el token está expirado (básico, sin decodificar)
  isTokenExpired: () => {
    const token = authUtils.getToken();
    if (!token) return true;

    try {
      // Decodificar payload JWT (base64)
      const payload = JSON.parse(atob(token.split('.')[1]));
      const exp = payload.exp * 1000; // Convertir a milisegundos
      return Date.now() >= exp;
    } catch (error) {
      console.error('Error al verificar expiración del token:', error);
      return true;
    }
  },

  // Verificar autenticación completa
  isAuthenticated: () => {
    return authUtils.hasToken() && !authUtils.isTokenExpired();
  }
};

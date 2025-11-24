const API_URL = 'http://localhost:8080/api/ingreso';

export const ingresoService = {
  login: async (credenciales) => {
    try {
      console.log('=== Iniciando login ===');
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
      
      // Guardar datos del usuario en localStorage
      if (data.usuario) {
        localStorage.setItem('usuario', JSON.stringify(data.usuario));
        console.log('Usuario guardado en localStorage:', data.usuario);
      }
      
      return data;
    } catch (error) {
      console.error('Error en login:', error);
      throw error;
    }
  },

  logout: () => {
    localStorage.removeItem('usuario');
    console.log('Usuario removido del localStorage');
  },

  obtenerUsuarioActual: () => {
    const usuarioStr = localStorage.getItem('usuario');
    return usuarioStr ? JSON.parse(usuarioStr) : null;
  },

  estaAutenticado: () => {
    return localStorage.getItem('usuario') !== null;
  }
};
const API_URL = 'http://localhost:8084/api/registro';

export const registroService = {
  registrarUsuario: async (datosUsuario) => {
    try {
      const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(datosUsuario)
      });
      
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Error al registrar usuario');
      }
      
      return await response.json();
    } catch (error) {
      console.error('Error en registro:', error);
      throw error;
    }
  },

  verificarRut: async (rut) => {
    try {
      const response = await fetch(`${API_URL}/verificar/${rut}`);
      if (!response.ok) throw new Error('Error al verificar RUT');
      return await response.json();
    } catch (error) {
      console.error('Error:', error);
      throw error;
    }
  }
};
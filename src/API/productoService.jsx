const API_URL = 'http://localhost:8083/api/productos';

export const productoService = {
  obtenerTodos: async () => {
    try {
      const response = await fetch(API_URL);
      if (!response.ok) throw new Error('Error al obtener productos');
      const data = await response.json();
      return data.data || [];
    } catch (error) {
      console.error('Error:', error);
      return [];
    }
  },

  obtenerPorId: async (id) => {
    try {
      const response = await fetch(`${API_URL}/${id}`);
      if (!response.ok) throw new Error('Producto no encontrado');
      const data = await response.json();
      return data.data;
    } catch (error) {
      console.error('Error:', error);
      throw error;
    }
  },

  crear: async (producto) => {
    try {
      const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(producto)
      });
      if (!response.ok) throw new Error('Error al crear producto');
      const data = await response.json();
      return data.data;
    } catch (error) {
      console.error('Error:', error);
      throw error;
    }
  },

  actualizar: async (id, producto) => {
    try {
      const response = await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(producto)
      });
      if (!response.ok) throw new Error('Error al actualizar producto');
      const data = await response.json();
      return data.data;
    } catch (error) {
      console.error('Error:', error);
      throw error;
    }
  },

  eliminar: async (id) => {
    try {
      const response = await fetch(`${API_URL}/${id}`, {
        method: 'DELETE'
      });
      if (!response.ok) throw new Error('Error al eliminar producto');
      return true;
    } catch (error) {
      console.error('Error:', error);
      throw error;
    }
  }
};
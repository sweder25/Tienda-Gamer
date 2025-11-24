const API_URL = 'http://localhost:8088/api/boletas';

export const boletaService = {
  obtenerTodas: async () => {
    try {
      const response = await fetch(API_URL);
      if (!response.ok) throw new Error('Error al obtener boletas');
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
      if (!response.ok) throw new Error('Boleta no encontrada');
      const data = await response.json();
      return data.data;
    } catch (error) {
      console.error('Error:', error);
      throw error;
    }
  },

  obtenerPorVentaId: async (ventaId) => {
    try {
      const response = await fetch(`${API_URL}/venta/${ventaId}`);
      if (!response.ok) throw new Error('Boleta no encontrada');
      const data = await response.json();
      return data.data;
    } catch (error) {
      console.error('Error:', error);
      throw error;
    }
  },

  generarBoleta: async (ventaId) => {
    try {
      const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ventaId })
      });
      if (!response.ok) throw new Error('Error al generar boleta');
      const data = await response.json();
      return data.data;
    } catch (error) {
      console.error('Error:', error);
      throw error;
    }
  }
};
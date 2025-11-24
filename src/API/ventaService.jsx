const API_URL = 'http://localhost:8087/api/ventas';

export const ventaService = {
  crearVenta: async (ventaData) => {
    try {
      console.log('=== CREANDO VENTA ===');
      console.log('URL completa:', API_URL);
      console.log('Datos:', JSON.stringify(ventaData, null, 2));
      
      const response = await fetch(API_URL, {
        method: 'POST',
        mode: 'cors',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        body: JSON.stringify(ventaData)
      });

      console.log('Response status:', response.status);
      console.log('Response headers:', Object.fromEntries(response.headers.entries()));

      const responseText = await response.text();
      console.log('Response text:', responseText);

      if (!response.ok) {
        throw new Error(`Error ${response.status}: ${responseText}`);
      }

      const data = JSON.parse(responseText);
      console.log('Data parseada:', data);
      
      if (data.data && data.data.id) {
        return data.data;
      }
      
      if (data.id) {
        return data;
      }

      throw new Error('No se recibió el ID de la venta');
      
    } catch (error) {
      console.error('=== ERROR ===');
      console.error('Tipo:', error.name);
      console.error('Mensaje:', error.message);
      console.error('Stack:', error.stack);
      throw error;
    }
  },

  obtenerVentasPorUsuario: async (usuarioId) => {
    try {
      const response = await fetch(`${API_URL}/usuario/${usuarioId}`);
      if (!response.ok) throw new Error(`Error ${response.status}`);
      const data = await response.json();
      return data.data || [];
    } catch (error) {
      console.error('Error al obtener ventas:', error);
      return [];
    }
  },

  obtenerVentaPorId: async (id) => {
    try {
      const response = await fetch(`${API_URL}/${id}`);
      if (!response.ok) throw new Error(`Error ${response.status}`);
      const data = await response.json();
      return data.data;
    } catch (error) {
      console.error('Error al obtener venta:', error);
      throw error;
    }
  }
};

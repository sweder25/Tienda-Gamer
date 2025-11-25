import axios from 'axios';
const API_BASE_URL = 'http://localhost:8085/api/tienda';

export const fetchBoletas = async () => {
    try {
        const response = await axios.get(`${API_BASE_URL}/boletas`);
        console.log('Response boletas COMPLETA:', response);
        console.log('Response boletas DATA:', response.data);
        console.log('Es array?:', Array.isArray(response.data));
        
        // Si viene como objeto con propiedad, ajusta aquí
        // Por ejemplo: return response.data.content || response.data.data || response.data;
        return response.data.data;
    } catch (error) {
        console.error('Error fetching boletas:', error);
        return []; // Retornar array vacío en caso de error
    }
};

export const fetchProductos = async () => {
    try {
        const response = await axios.get(`${API_BASE_URL}/productos`);
        console.log('Response productos:', response.data);
        return response.data.data;
    } catch (error) {
        console.error('Error fetching productos:', error);
        return [];
    }
};

export const fetchVentas = async () => {
    try {
        const response = await axios.get(`${API_BASE_URL}/ventas`);
        console.log('Response ventas:', response.data);
        return response.data.data;
    } catch (error) {
        console.error('Error fetching ventas:', error);
        return [];
    }
};

export const fetchUsuarios = async () => {
    try {
        const response = await axios.get(`${API_BASE_URL}/usuarios`);
        console.log('Response usuarios:', response.data);
        return response.data.data;
    } catch (error) {
        console.error('Error fetching usuarios:', error);
        return [];
    }
};

export const fetchRegistros = async () => {
    try {
        const response = await axios.get(`${API_BASE_URL}/registro`);
        console.log('Response registros:', response.data);
        return response.data.data;
    } catch (error) {
        console.error('Error fetching registros:', error);
        return [];
    }
};
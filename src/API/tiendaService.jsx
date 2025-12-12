import axios from 'axios';
import { authUtils } from '../utils/authUtils';

const API_BASE_URL = 'http://localhost:8085/api/tienda';

// Configurar interceptor para agregar JWT automáticamente
axios.interceptors.request.use(
    (config) => {
        const token = authUtils.getToken();
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Interceptor para manejar errores de autenticación
axios.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response && error.response.status === 401) {
            // Token inválido o expirado
            console.error('Token inválido o expirado');
            authUtils.clearAuth();
            window.location.href = '/inicio';
        }
        return Promise.reject(error);
    }
);

export const fetchBoletas = async () => {
    try {
        const response = await axios.get(`${API_BASE_URL}/boletas`);
        console.log('Response boletas COMPLETA:', response);
        console.log('Response boletas DATA:', response.data);
        console.log('Es array?:', Array.isArray(response.data));

        return response.data.data;
    } catch (error) {
        console.error('Error fetching boletas:', error);
        return [];
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
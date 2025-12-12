import { authUtils } from './authUtils';

/**
 * Wrapper para fetch que maneja automáticamente errores 401
 * y redirige a login si el token es inválido
 */
export const fetchWithAuth = async (url, options = {}) => {
  // Merge headers con el token JWT
  const headers = {
    ...authUtils.getAuthHeaders(),
    ...options.headers
  };

  const response = await fetch(url, {
    ...options,
    headers
  });

  // Si recibimos 401, el token es inválido o expiró
  if (response.status === 401) {
    console.error('Token inválido o expirado - Redirigiendo a login');
    authUtils.clearAuth();
    window.location.href = '/inicio';
    throw new Error('Sesión expirada. Por favor inicia sesión nuevamente.');
  }

  return response;
};

/**
 * Helper para hacer GET requests con autenticación
 */
export const getWithAuth = async (url) => {
  return fetchWithAuth(url, { method: 'GET' });
};

/**
 * Helper para hacer POST requests con autenticación
 */
export const postWithAuth = async (url, data) => {
  return fetchWithAuth(url, {
    method: 'POST',
    body: JSON.stringify(data)
  });
};

/**
 * Helper para hacer PUT requests con autenticación
 */
export const putWithAuth = async (url, data) => {
  return fetchWithAuth(url, {
    method: 'PUT',
    body: JSON.stringify(data)
  });
};

/**
 * Helper para hacer DELETE requests con autenticación
 */
export const deleteWithAuth = async (url) => {
  return fetchWithAuth(url, { method: 'DELETE' });
};

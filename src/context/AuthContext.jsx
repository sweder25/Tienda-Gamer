import React, { createContext, useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ingresoService } from '../API/IngresoService';
import { authUtils } from '../utils/authUtils';

const AuthContext = createContext();

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth debe usarse dentro de AuthProvider');
    }
    return context;
};

export const AuthProvider = ({ children }) => {
    const [usuario, setUsuario] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const verificarAuth = async () => {
            // Verificar si hay token y usuario guardados
            const usuarioGuardado = ingresoService.obtenerUsuarioActual();
            const tokenValido = authUtils.isAuthenticated();
            
            if (usuarioGuardado && tokenValido) {
                setUsuario(usuarioGuardado);
            } else if (!tokenValido) {
                // Si el token expiró, limpiar todo
                ingresoService.logout();
                setUsuario(null);
            }
            
            setLoading(false);
        };
        
        verificarAuth();
    }, []);

    const login = async (credenciales) => {
        try {
            const response = await ingresoService.login(credenciales);
            setUsuario(response.usuario);
            return response;
        } catch (error) {
            throw error;
        }
    };

    const register = async (userData) => {
        try {
            const response = await ingresoService.register(userData);
            // Si el registro devuelve token, iniciar sesión automáticamente
            if (response.usuario) {
                setUsuario(response.usuario);
            }
            return response;
        } catch (error) {
            throw error;
        }
    };

    const logout = () => {
        ingresoService.logout();
        setUsuario(null);
        navigate('/inicio', { replace: true });
    };

    const value = {
        usuario,
        login,
        register,
        logout,
        isAuthenticated: !!usuario && authUtils.isAuthenticated(),
        loading
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};
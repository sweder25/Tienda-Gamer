import React, { createContext, useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ingresoService } from '../API/IngresoService';

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
        const usuarioGuardado = ingresoService.obtenerUsuarioActual();
        if (usuarioGuardado) {
            setUsuario(usuarioGuardado);
        }
        setLoading(false);
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

    const logout = () => {
        ingresoService.logout();
        setUsuario(null);
        navigate('/inicio', { replace: true });
    };

    const value = {
        usuario,
        login,
        logout,
        isAuthenticated: !!usuario,
        loading
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};
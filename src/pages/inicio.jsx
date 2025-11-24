import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Inicio() {
    const navigate = useNavigate();
    const { login } = useAuth();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [formData, setFormData] = useState({
        email: '',
        password: ''
    });

    const handleChange = (e) => {
        const { id, value } = e.target;
        const field = id.replace('input', '').toLowerCase();
        setFormData(prev => ({
            ...prev,
            [field]: value
        }));
        if (error) setError(null);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        // Validaciones básicas
        if (!formData.email || !formData.password) {
            setError('Por favor completa todos los campos.');
            return;
        }

        setLoading(true);

        try {
            const response = await login({
                email: formData.email.toLowerCase().trim(),
                password: formData.password
            });

            // Login exitoso
            alert(`¡Bienvenido ${response.usuario.nombre}!`);
            navigate('/'); // Redirigir al home
            
        } catch (error) {
            setError(error.message || 'Credenciales inválidas. Intenta nuevamente.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="d-flex justify-content-center align-items-center bg-light" 
             style={{background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', minHeight: 'calc(100vh - 60px)'}}>
            
            <div className="card shadow border-0 p-4" style={{ width: '100%', maxWidth: '400px' }}>
                <form onSubmit={handleSubmit}>
                    
                    {/* Encabezado */}
                    <div className="text-center mb-4">
                        <img className="mb-3" src="https://getbootstrap.com/docs/4.3/assets/brand/bootstrap-solid.svg" alt="" width="72" height="72" />
                        <h1 className="h4 font-weight-normal">Inicio de Sesión</h1>
                        <p className="text-muted">Accede a tu cuenta</p>
                    </div>

                    {/* Mostrar errores */}
                    {error && (
                        <div className="alert alert-danger alert-dismissible fade show" role="alert">
                            <small>{error}</small>
                            <button type="button" className="close" onClick={() => setError(null)}>
                                <span>&times;</span>
                            </button>
                        </div>
                    )}

                    {/* Email */}
                    <div className="form-group mb-3">
                        <label htmlFor="inputEmail" className="sr-only">Email</label>
                        <input 
                            type="email" 
                            id="inputEmail" 
                            className="form-control" 
                            placeholder="Email" 
                            value={formData.email}
                            onChange={handleChange}
                            required 
                            autoFocus
                            disabled={loading}
                        />
                    </div>

                    {/* Contraseña */}
                    <div className="form-group mb-4">
                        <label htmlFor="inputPassword" className="sr-only">Contraseña</label>
                        <input 
                            type="password" 
                            id="inputPassword" 
                            className="form-control" 
                            placeholder="Contraseña" 
                            value={formData.password}
                            onChange={handleChange}
                            required
                            disabled={loading}
                        />
                    </div>

                    {/* Botón de Login */}
                    <button 
                        className="btn btn-lg btn-primary btn-block mb-3" 
                        type="submit"
                        disabled={loading}
                    >
                        {loading ? (
                            <>
                                <span className="spinner-border spinner-border-sm mr-2" role="status" aria-hidden="true"></span>
                                Iniciando sesión...
                            </>
                        ) : (
                            'Iniciar Sesión'
                        )}
                    </button>

                    {/* Link para registro */}
                    <div className="text-center">
                        <span className="text-muted">¿No tienes cuenta? </span>
                        <a href="/registro" className="font-weight-bold">Regístrate aquí</a>
                    </div>

                </form>
            </div>
        </div>
    );
}
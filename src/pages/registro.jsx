import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { registroService } from '../API/registroService';

export default function Registro() {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [formData, setFormData] = useState({
        nombre: '',
        email: '',
        password: '',
        confirmarPassword: '',
        direccion: ''
    });

    const handleChange = (e) => {
        const { id, value } = e.target;
        const field = id.replace('input', '');
        const fieldName = field.charAt(0).toLowerCase() + field.slice(1);
        setFormData(prev => ({
            ...prev,
            [fieldName]: value
        }));
        if (error) setError(null);
    };

    const validarEmail = (email) => {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        
        // Validaciones
        if (!formData.nombre || formData.nombre.length < 3) {
            setError('El nombre debe tener al menos 3 caracteres.');
            return;
        }

        if (!validarEmail(formData.email)) {
            setError('Email inválido.');
            return;
        }

        if (formData.password.length < 6) {
            setError('La contraseña debe tener al menos 6 caracteres.');
            return;
        }

        if (formData.password !== formData.confirmarPassword) {
            setError('Las contraseñas no coinciden.');
            return;
        }

        if (!formData.direccion || formData.direccion.length < 5) {
            setError('La dirección debe tener al menos 5 caracteres.');
            return;
        }

        setLoading(true);

        try {
            // Preparar datos según el modelo Registro.java
            const datosRegistro = {
                nombre: formData.nombre.trim(),
                email: formData.email.toLowerCase().trim(),
                password: formData.password,
                direccion: formData.direccion.trim(),
                rol: 'USER' // Valor por defecto
            };

            const response = await registroService.registrarUsuario(datosRegistro);
            
            // Registro exitoso
            alert('¡Registro exitoso! Ahora puedes iniciar sesión.');
            navigate('/inicio');
            
        } catch (error) {
            setError(error.message || 'Error al registrar. Intenta nuevamente.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="d-flex justify-content-center align-items-center bg-light" 
             style={{background: 'linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)', minHeight: 'calc(100vh - 60px)' }}>
            
            <div className="card shadow border-0 p-4" style={{ width: '100%', maxWidth: '400px' }}>
                <form onSubmit={handleSubmit}>
                    
                    {/* Encabezado */}
                    <div className="text-center mb-3">
                        <img className="mb-2" src="https://getbootstrap.com/docs/4.3/assets/brand/bootstrap-solid.svg" alt="" width="50" height="50" />
                        <h1 className="h5 font-weight-normal mb-0">Crear Cuenta</h1>
                        <p className="text-muted small">Únete a la Tienda Gamer</p>
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

                    {/* Nombre */}
                    <div className="form-group text-left mb-2">
                        <label htmlFor="inputNombre" className="small text-muted font-weight-bold mb-0">NOMBRE COMPLETO</label>
                        <input 
                            type="text" 
                            id="inputNombre" 
                            className="form-control form-control-sm" 
                            placeholder="Ej: Juan Pérez González" 
                            value={formData.nombre}
                            onChange={handleChange}
                            required
                            autoFocus
                            disabled={loading}
                        />
                    </div>

                    {/* Email */}
                    <div className="form-group text-left mb-2">
                        <label htmlFor="inputEmail" className="small text-muted font-weight-bold mb-0">EMAIL</label>
                        <input 
                            type="email" 
                            id="inputEmail" 
                            className="form-control form-control-sm" 
                            placeholder="nombre@correo.com" 
                            value={formData.email}
                            onChange={handleChange}
                            required
                            disabled={loading}
                        />
                    </div>

                    {/* Dirección */}
                    <div className="form-group text-left mb-2">
                        <label htmlFor="inputDireccion" className="small text-muted font-weight-bold mb-0">DIRECCIÓN</label>
                        <input 
                            type="text" 
                            id="inputDireccion" 
                            className="form-control form-control-sm" 
                            placeholder="Calle, Número, Comuna" 
                            value={formData.direccion}
                            onChange={handleChange}
                            required
                            disabled={loading}
                        />
                    </div>

                    {/* Contraseña */}
                    <div className="form-group text-left mb-2">
                        <label htmlFor="inputPassword" className="small text-muted font-weight-bold mb-0">CONTRASEÑA</label>
                        <input 
                            type="password" 
                            id="inputPassword" 
                            className="form-control form-control-sm" 
                            placeholder="Mínimo 6 caracteres" 
                            value={formData.password}
                            onChange={handleChange}
                            required
                            disabled={loading}
                        />
                    </div>

                    {/* Confirmar Contraseña */}
                    <div className="form-group text-left mb-4">
                        <label htmlFor="inputConfirmarPassword" className="small text-muted font-weight-bold mb-0">CONFIRMAR CONTRASEÑA</label>
                        <input 
                            type="password" 
                            id="inputConfirmarPassword" 
                            className="form-control form-control-sm" 
                            placeholder="Repite tu contraseña" 
                            value={formData.confirmarPassword}
                            onChange={handleChange}
                            required
                            disabled={loading}
                        />
                    </div>

                    {/* Botón de Registro */}
                    <button 
                        className="btn btn-sm btn-success btn-block font-weight-bold mb-3" 
                        type="submit"
                        disabled={loading}
                    >
                        {loading ? (
                            <>
                                <span className="spinner-border spinner-border-sm mr-2" role="status" aria-hidden="true"></span>
                                REGISTRANDO...
                            </>
                        ) : (
                            'REGISTRARSE'
                        )}
                    </button>
                    
                    {/* Link para volver al Login */}
                    <div className="text-center">
                        <span className="small text-muted">¿Ya tienes cuenta? </span>
                        <a href="/inicio" className="small font-weight-bold text-dark">Inicia Sesión aquí</a>
                    </div>

                </form>
            </div>
        </div>
    );
}
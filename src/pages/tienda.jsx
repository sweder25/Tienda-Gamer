import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { fetchBoletas, fetchProductos, fetchVentas, fetchUsuarios, fetchRegistros } from '../API/tiendaService';
import BoletasTable from '../components/boletasTable';
import ProductosTable from '../components/productosTable';
import VentasTable from '../components/ventasTable';
import UsuariosTable from '../components/usuarioTable';
import RegistrosTable from '../components/RegistrosTable';

const Tienda = () => {
    const { usuario } = useAuth();
    const navigate = useNavigate();
    const [boletas, setBoletas] = useState([]);
    const [productos, setProductos] = useState([]);
    const [ventas, setVentas] = useState([]);
    const [usuarios, setUsuarios] = useState([]);
    const [registros, setRegistros] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isAdmin, setIsAdmin] = useState(true);

    useEffect(() => {
        verificarAcceso();
    }, [usuario, navigate]);

    const verificarAcceso = async () => {
        try {
            if (!usuario) {
                navigate('/login');
                return;
            }

            // Obtener registros para verificar el rol
            const registrosData = await fetchRegistros();
            console.log('Registros data:', registrosData);

            // Buscar el registro del usuario actual
            const registroUsuario = registrosData.find(
                registro => registro.usuarioId === usuario.id || registro.email === usuario.email
            );

            console.log('Registro usuario:', registroUsuario);

            // Verificar si el usuario tiene rol de ADMIN
            if (!registroUsuario || registroUsuario.rol !== 'ADMIN') {
                alert('Acceso denegado. Solo administradores pueden acceder.');
                navigate('/');
                return;
            }

            setIsAdmin(true);
            cargarDatos();

        } catch (error) {
            console.error('Error al verificar acceso:', error);
            setError('Error al verificar permisos de acceso.');
            setLoading(false);
        }
    };

    const cargarDatos = async () => {
        try {
            setLoading(true);
            setError(null);
            
            console.log('Iniciando carga de datos...');
            
            const [boletasData, productosData, ventasData, usuariosData, registrosData] = await Promise.all([
                fetchBoletas(),
                fetchProductos(),
                fetchVentas(),
                fetchUsuarios(),
                fetchRegistros()
            ]);

            console.log('Boletas recibidas:', boletasData);
            console.log('Productos recibidos:', productosData);
            console.log('Ventas recibidas:', ventasData);
            console.log('Usuarios recibidos:', usuariosData);
            console.log('Registros recibidos:', registrosData);

            setBoletas(Array.isArray(boletasData) ? boletasData : []);
            setProductos(Array.isArray(productosData) ? productosData : []);
            setVentas(Array.isArray(ventasData) ? ventasData : []);
            setUsuarios(Array.isArray(usuariosData) ? usuariosData : []);
            setRegistros(Array.isArray(registrosData) ? registrosData : []);
            
        } catch (error) {
            console.error('Error al cargar datos:', error);
            setError('Error al cargar los datos. Por favor, intenta recargar.');
        } finally {
            setLoading(false);
        }
    };

    const recargarDatos = async () => {
        await cargarDatos();
    };

    // Si no hay usuario o no es ADMIN, no mostrar nada (ya redirigió)
    if (!usuario || !isAdmin) {
        return null;
    }

    if (loading) {
        return (
            <div className="container py-5 text-center">
                <div className="spinner-border text-primary" role="status" style={{ width: '3rem', height: '3rem' }}>
                    <span className="visually-hidden">Cargando...</span>
                </div>
                <p className="mt-3 text-muted">Cargando datos de la tienda...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="container py-5">
                <div className="alert alert-danger d-flex align-items-center" role="alert">
                    <i className="bi bi-exclamation-triangle-fill me-3" style={{ fontSize: '1.5rem' }}></i>
                    <div className="flex-grow-1">
                        <strong>Error:</strong> {error}
                    </div>
                    <button className="btn btn-outline-danger" onClick={recargarDatos}>
                        <i className="bi bi-arrow-clockwise"></i> Reintentar
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="bg-light min-vh-100 py-5">
            <div className="container">
                <div className="d-flex justify-content-between align-items-center mb-5">
                    <div>
                        <h1 className="display-5 fw-bold text-primary mb-2">
                            <i className="bi bi-shop"></i> Panel de Tienda
                        </h1>
                        <p className="lead text-muted mb-0">
                            Revisión de datos del sistema
                        </p>
                    </div>
                    <button 
                        className="btn btn-outline-primary btn-lg" 
                        onClick={recargarDatos}
                        title="Recargar datos"
                    >
                        <i className="bi bi-arrow-clockwise"></i> Recargar
                    </button>
                </div>

                {/* Boletas */}
                <div className="card shadow-sm mb-4">
                    <div className="card-header bg-primary text-white">
                        <h3 className="mb-0">
                            <i className="bi bi-receipt"></i> Boletas ({boletas.length})
                        </h3>
                    </div>
                    <div className="card-body">
                        <BoletasTable data={boletas} />
                    </div>
                </div>

                {/* Productos */}
                <div className="card shadow-sm mb-4">
                    <div className="card-header bg-success text-white">
                        <h3 className="mb-0">
                            <i className="bi bi-box-seam"></i> Productos ({productos.length})
                        </h3>
                    </div>
                    <div className="card-body">
                        <ProductosTable data={productos} />
                    </div>
                </div>

                {/* Ventas */}
                <div className="card shadow-sm mb-4">
                    <div className="card-header bg-info text-white">
                        <h3 className="mb-0">
                            <i className="bi bi-cart-check"></i> Ventas ({ventas.length})
                        </h3>
                    </div>
                    <div className="card-body">
                        <VentasTable data={ventas} />
                    </div>
                </div>

                {/* Usuarios */}
                <div className="card shadow-sm mb-4">
                    <div className="card-header bg-warning text-dark">
                        <h3 className="mb-0">
                            <i className="bi bi-people"></i> Usuarios ({usuarios.length})
                        </h3>
                    </div>
                    <div className="card-body">
                        <UsuariosTable data={usuarios} />
                    </div>
                </div>

                {/* Registros */}
                <div className="card shadow-sm mb-4">
                    <div className="card-header bg-secondary text-white">
                        <h3 className="mb-0">
                            <i className="bi bi-journal-text"></i> Registros ({registros.length})
                        </h3>
                    </div>
                    <div className="card-body">
                        <RegistrosTable data={registros} />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Tienda;
import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
    const { usuario, logout, isAuthenticated } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/inicio');
    };

    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
            <div className="container">
                <Link className="navbar-brand" to="/">
                    <strong>Tienda Gamer</strong>
                </Link>
                
                <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav">
                    <span className="navbar-toggler-icon"></span>
                </button>
                
                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav ml-auto">
                        <li className="nav-item">
                            <Link className="nav-link" to="/">Inicio</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/catalogo">Catálogo</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/carrito">
                                <i className="bi bi-cart"></i> Carrito
                            </Link>
                        </li>
                        
                        {isAuthenticated ? (
                            <>
                                <li className="nav-item">
                                    <span className="nav-link text-light">
                                        <i className="bi bi-person-circle"></i> {usuario?.nombre}
                                    </span>
                                </li>
                                <li className="nav-item">
                                    <button 
                                        className="nav-link btn btn-link" 
                                        onClick={handleLogout}
                                    >
                                        Cerrar Sesión
                                    </button>
                                </li>
                            </>
                        ) : (
                            <>
                                <li className="nav-item">
                                    <Link className="nav-link" to="/inicio">Iniciar Sesión</Link>
                                </li>
                                <li className="nav-item">
                                    <Link className="nav-link" to="/registro">Registrarse</Link>
                                </li>
                            </>
                        )}
                    </ul>
                </div>
            </div>
        </nav>
    );
}
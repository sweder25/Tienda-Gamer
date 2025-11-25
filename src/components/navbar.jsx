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
                <button
                    className="navbar-toggler"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#navbarNav"
                    aria-controls="navbarNav"
                    aria-expanded="false"
                    aria-label="Toggle navigation"
                >
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                        <li className="nav-item">
                            <Link className="nav-link" to="/">Inicio</Link>
                        </li>
                        {usuario?.rol === 'ADMIN' && (
                            <li className="nav-item">
                                <Link className="nav-link" to="/tienda">Tienda</Link>
                            </li>
                        )}
                        <li className="nav-item">
                            <Link className="nav-link" to="/catalogo">Catálogo</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/boleta">
                                <i className="bi bi-receipt"></i> Mis Boletas
                            </Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/carrito">
                                <i className="bi bi-cart"></i> Carrito
                            </Link>
                        </li>
                    </ul>
                    <ul className="navbar-nav ms-auto mb-2 mb-lg-0">
                        {isAuthenticated ? (
                            <>
                                <li className="nav-item dropdown d-flex align-items-center">
                                    <span
                                        className="nav-link dropdown-toggle text-light"
                                        id="usuarioDropdown"
                                        role="button"
                                        data-bs-toggle="dropdown"
                                        aria-expanded="false"
                                        style={{ cursor: 'pointer' }}
                                    >
                                        <i className="bi bi-person-circle"></i> {usuario?.nombre}
                                    </span>
                                    <ul className="dropdown-menu dropdown-menu-end" aria-labelledby="usuarioDropdown">
                                        <li>
                                            <Link className="dropdown-item" to="/boleta">
                                                <i className="bi bi-receipt"></i> Mis Boletas
                                            </Link>
                                        </li>
                                        <li>
                                            <button
                                                className="dropdown-item"
                                                onClick={handleLogout}
                                            >
                                                Cerrar Sesión
                                            </button>
                                        </li>
                                    </ul>
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
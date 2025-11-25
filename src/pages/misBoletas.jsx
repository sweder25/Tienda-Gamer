import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { fetchBoletas } from '../API/tiendaService';

export default function MisBoletas() {
    const { usuario } = useAuth();
    const [boletas, setBoletas] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const cargarBoletas = async () => {
            try {
                if (usuario?.id) {
                    const todasBoletas = await fetchBoletas();

                    // Filtrar solo las del usuario actual
                    const boletasUsuario = todasBoletas.filter(
                        b => b.usuarioId === usuario.id
                    );

                    setBoletas(boletasUsuario);
                }
            } catch (error) {
                console.error("Error al cargar boletas:", error);
            } finally {
                setLoading(false);
            }
        };

        cargarBoletas();
    }, [usuario]);

    if (loading) {
        return (
            <div className="container py-5 text-center">
                <div className="spinner-border text-primary" style={{ width: "3rem", height: "3rem" }}></div>
                <p className="mt-3 text-muted">Cargando tus boletas...</p>
            </div>
        );
    }

    return (
        <div className="bg-light min-vh-100 py-5">
            <div className="container">

                {/* ENCABEZADO */}
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <div>
                        <h1 className="display-6 fw-bold text-primary mb-1">
                            <i className="bi bi-receipt"></i> Mis Boletas
                        </h1>
                        <p className="text-muted">
                            Historial de compras asociadas a tu cuenta
                        </p>
                    </div>
                </div>

                {/* TARJETA PRINCIPAL */}
                <div className="card shadow-sm">
                    <div className="card-header bg-primary text-white">
                        <h4 className="mb-0">
                            <i className="bi bi-file-earmark-text"></i> Boletas ({boletas.length})
                        </h4>
                    </div>

                    <div className="card-body">

                        {/* SIN BOLETAS */}
                        {boletas.length === 0 ? (
                            <div className="text-center py-5">
                                <i className="bi bi-inbox text-secondary" style={{ fontSize: "3.5rem" }}></i>
                                <h5 className="mt-3 text-muted">No tienes boletas registradas</h5>
                                <p className="text-muted">
                                    Tus compras aparecerán aquí automáticamente.
                                </p>
                            </div>
                        ) : (
                            <div className="table-responsive">
                                <table className="table table-striped align-middle">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Fecha</th>
                                            <th>Total</th>
                                            <th>Dirección Cliente</th>
                                            <th>Metodo de pago</th>
                                            <th>Codigo</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {boletas.map(boleta => (
                                            <tr key={boleta.id}>
                                                <td>{boleta.id}</td>
                                                <td>{boleta.fechaEmision}</td>
                                                <td>
                                                    <span className="fw-bold text-success">
                                                        ${boleta.total?.toLocaleString("es-CL")}
                                                    </span>
                                                </td>
                                                <td>{boleta.direccionEnvio}</td>
                                                <td>{boleta.metodoPago}</td>
                                                <td>{boleta.numero}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        )}

                    </div>
                </div>
            </div>
        </div>
    );
}

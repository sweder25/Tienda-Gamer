import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCarrito } from '../context/CarritoContext';
import { useAuth } from '../context/AuthContext';
import { ventaService } from '../API/ventaService';

export default function Carrito() {
  const navigate = useNavigate();
  const { carrito, actualizarCantidad, eliminarDelCarrito, calcularTotal, limpiarCarrito } = useCarrito();
  const { usuario, isAuthenticated } = useAuth();
  
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async () => {
    setLoading(true);
    setError(null);

    try {
      if (!isAuthenticated || !usuario || !usuario.id) {
        throw new Error('Debes iniciar sesión para realizar la compra');
      }

      if (carrito.length === 0) {
        throw new Error('El carrito está vacío');
      }

      console.log('=== INICIANDO COMPRA ===');
      console.log('Usuario:', usuario);
      console.log('Carrito:', carrito);

      const productosData = carrito.map(item => ({
        productoId: item.id,
        cantidad: item.cantidad
      }));

      const ventaData = {
        usuarioId: usuario.id,
        nombreCliente: usuario.nombre,
        apellidoCliente: usuario.apellido || '',
        emailCliente: usuario.email,
        direccion: usuario.direccion || 'Dirección no especificada',
        region: 'Metropolitana',
        codigoPostal: '',
        metodoPago: 'CREDITO',
        productos: productosData
      };

      console.log('Enviando datos de venta:', ventaData);

      const ventaCreada = await ventaService.crearVenta(ventaData);
      
      console.log('Venta creada exitosamente:', ventaCreada);

      limpiarCarrito();
      
      navigate(`/boleta/${ventaCreada.id}`);

    } catch (error) {
      console.error('Error en compra:', error);
      setError(error.message || 'Error al procesar la compra');
    } finally {
      setLoading(false);
    }
  };

  const total = calcularTotal();

  if (carrito.length === 0) {
    return (
      <div className="container py-5 text-center">
        <div className="mb-4">
          <i className="bi bi-cart-x" style={{ fontSize: "5rem", color: "#6c757d" }}></i>
        </div>
        <h2>Tu carrito está vacío</h2>
        <p className="text-muted mb-4">Agrega productos desde el catálogo</p>
        <button className="btn btn-primary btn-lg" onClick={() => navigate('/catalogo')}>
          <i className="bi bi-shop"></i> Ver Catálogo
        </button>
      </div>
    );
  }

  return (
    <div className="container py-5">
      <h2 className="mb-4">
        <i className="bi bi-cart3"></i> Mi Carrito
      </h2>

      <div className="row">
        <div className="col-md-8 mb-4">
          <div className="card">
            <div className="card-header bg-primary text-white">
              <h5 className="mb-0">Productos ({carrito.length})</h5>
            </div>
            <ul className="list-group list-group-flush">
              {carrito.map((producto) => (
                <li key={producto.id} className="list-group-item">
                  <div className="row align-items-center">
                    <div className="col-md-2 text-center">
                      <div 
                        className="bg-gradient d-flex align-items-center justify-content-center"
                        style={{ 
                          height: "80px", 
                          borderRadius: "8px",
                          background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)"
                        }}
                      >
                        <i className="bi bi-controller text-white" style={{ fontSize: "2rem" }}></i>
                      </div>
                    </div>
                    
                    <div className="col-md-4">
                      <h6 className="mb-1">{producto.nombre}</h6>
                      <small className="text-muted">{producto.categoria}</small>
                      <div>
                        <span className="badge badge-info">Stock: {producto.stock}</span>
                      </div>
                    </div>
                    
                    <div className="col-md-2 text-center">
                      <small className="text-muted d-block">Precio</small>
                      <strong>${producto.precio.toLocaleString('es-CL')}</strong>
                    </div>
                    
                    <div className="col-md-2 text-center">
                      <small className="text-muted d-block mb-2">Cantidad</small>
                      <div className="btn-group" role="group">
                        <button 
                          className="btn btn-sm btn-outline-secondary"
                          onClick={() => actualizarCantidad(producto.id, producto.cantidad - 1)}
                        >
                          <i className="bi bi-dash"></i>
                        </button>
                        <button className="btn btn-sm btn-outline-secondary" disabled>
                          {producto.cantidad}
                        </button>
                        <button 
                          className="btn btn-sm btn-outline-secondary"
                          onClick={() => actualizarCantidad(producto.id, producto.cantidad + 1)}
                          disabled={producto.cantidad >= producto.stock}
                        >
                          <i className="bi bi-plus"></i>
                        </button>
                      </div>
                    </div>
                    
                    <div className="col-md-2 text-center">
                      <div className="mb-2">
                        <strong className="text-success">
                          ${(producto.precio * producto.cantidad).toLocaleString('es-CL')}
                        </strong>
                      </div>
                      <button 
                        className="btn btn-sm btn-danger"
                        onClick={() => eliminarDelCarrito(producto.id)}
                      >
                        <i className="bi bi-trash"></i>
                      </button>
                    </div>
                  </div>
                </li>
              ))}
            </ul>
          </div>
        </div>

        <div className="col-md-4">
          <div className="card sticky-top" style={{ top: "20px" }}>
            <div className="card-header bg-success text-white">
              <h5 className="mb-0">Resumen de Compra</h5>
            </div>
            <div className="card-body">
              
              {isAuthenticated && usuario && (
                <div className="mb-3 p-3 bg-light rounded">
                  <h6 className="mb-2">
                    <i className="bi bi-person-circle"></i> Datos de Envío
                  </h6>
                  <small className="d-block"><strong>Nombre:</strong> {usuario.nombre}</small>
                  <small className="d-block"><strong>Email:</strong> {usuario.email}</small>
                  <small className="d-block"><strong>Dirección:</strong> {usuario.direccion || 'No especificada'}</small>
                </div>
              )}

              <div className="border-top pt-3">
                <div className="d-flex justify-content-between mb-2">
                  <span>Subtotal</span>
                  <strong>${total.toLocaleString('es-CL')}</strong>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>Envío</span>
                  <strong className="text-success">GRATIS</strong>
                </div>
                <hr />
                <div className="d-flex justify-content-between mb-3">
                  <h5>Total</h5>
                  <h5 className="text-success">${total.toLocaleString('es-CL')}</h5>
                </div>
              </div>

              {error && (
                <div className="alert alert-danger" role="alert">
                  <small><i className="bi bi-exclamation-circle"></i> {error}</small>
                </div>
              )}

              <button 
                className="btn btn-success btn-lg btn-block w-100"
                onClick={handleSubmit}
                disabled={loading || !isAuthenticated}
              >
                {loading ? (
                  <>
                    <span className="spinner-border spinner-border-sm me-2"></span>
                    Procesando...
                  </>
                ) : !isAuthenticated ? (
                  <>
                    <i className="bi bi-lock"></i> Inicia Sesión
                  </>
                ) : (
                  <>
                    <i className="bi bi-credit-card"></i> Confirmar Compra
                  </>
                )}
              </button>

              {!isAuthenticated && (
                <div className="alert alert-warning mt-3 mb-0" role="alert">
                  <small>
                    <i className="bi bi-exclamation-triangle"></i> 
                    Debes <a href="/inicio" className="alert-link">iniciar sesión</a> para completar tu compra.
                  </small>
                </div>
              )}

              <div className="mt-3 text-center">
                <small className="text-muted">
                  <i className="bi bi-shield-check"></i> Compra 100% segura
                </small>
              </div>
            </div>
          </div>

          <button 
            className="btn btn-outline-primary btn-block w-100 mt-3"
            onClick={() => navigate('/catalogo')}
          >
            <i className="bi bi-arrow-left"></i> Seguir Comprando
          </button>
        </div>
      </div>
    </div>
  );
}
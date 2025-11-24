import React, { useEffect, useState } from 'react';
import { boletaService } from '../API/boletaService';
import { useAuth } from '../context/AuthContext';

export default function MisBoletas() {
  const { usuario } = useAuth();
  const [boletas, setBoletas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [boletaSeleccionada, setBoletaSeleccionada] = useState(null);
  const [recargando, setRecargando] = useState(false);

  useEffect(() => {
    if (usuario) {
      cargarBoletas();
    } else {
      setLoading(false);
      setError('Debes iniciar sesión para ver tus boletas');
    }
  }, [usuario]);

  const cargarBoletas = async () => {
    try {
      setLoading(true);
      setError(null);
      
      if (!usuario || !usuario.id) {
        throw new Error('Usuario no identificado');
      }

      console.log('Cargando boletas para usuario:', usuario.id);
      
      const data = await boletaService.obtenerTodas();
      console.log('Todas las boletas:', data);
      
      // Filtrar boletas por el ID del usuario actual
      const boletasUsuario = data.filter(boleta => {
        console.log('Comparando:', boleta.usuarioId, 'con', usuario.id);
        return boleta.usuarioId === usuario.id || 
               boleta.usuarioId === Number(usuario.id) ||
               boleta.emailCliente === usuario.email;
      });
      
      console.log('Boletas filtradas del usuario:', boletasUsuario);
      
      setBoletas(boletasUsuario);
    } catch (error) {
      console.error('Error al cargar boletas:', error);
      setError('Error al cargar boletas. Por favor, intenta recargar.');
    } finally {
      setLoading(false);
    }
  };

  const recargarBoletas = async () => {
    try {
      setRecargando(true);
      setError(null);
      await new Promise(resolve => setTimeout(resolve, 500));
      await cargarBoletas();
    } catch (error) {
      console.error('Error al recargar:', error);
      setError('Error al recargar boletas');
    } finally {
      setRecargando(false);
    }
  };

  const verDetalle = (boleta) => {
    setBoletaSeleccionada(boleta);
  };

  const descargarBoleta = (boleta) => {
    if (!boleta) return;

    let productosHtml = '<p>Detalle no disponible</p>';
    
    if (boleta.detalleProductos) {
      try {
        const productos = JSON.parse(boleta.detalleProductos);
        productosHtml = `
          <table>
            <thead>
              <tr>
                <th>Producto</th>
                <th>Cantidad</th>
                <th>Precio Unit.</th>
                <th>Subtotal</th>
              </tr>
            </thead>
            <tbody>
              ${productos.map(item => `
                <tr>
                  <td>Producto ID ${item.productoId}</td>
                  <td>${item.cantidad}</td>
                  <td>$${item.precioUnitario?.toLocaleString('es-CL') || '0'}</td>
                  <td>$${item.subtotal?.toLocaleString('es-CL') || '0'}</td>
                </tr>
              `).join('')}
            </tbody>
          </table>
        `;
      } catch (e) {
        console.error('Error al parsear productos:', e);
      }
    }

    const contenido = `
      <!DOCTYPE html>
      <html>
      <head>
        <title>Boleta ${boleta.numero}</title>
        <meta charset="UTF-8">
        <style>
          body { font-family: Arial, sans-serif; padding: 20px; max-width: 800px; margin: 0 auto; }
          .header { text-align: center; border-bottom: 2px solid #333; padding-bottom: 10px; margin-bottom: 20px; }
          .info { margin: 20px 0; }
          .info-row { display: flex; justify-content: space-between; margin: 10px 0; padding: 5px 0; }
          .productos { margin: 20px 0; }
          table { width: 100%; border-collapse: collapse; margin-top: 10px; }
          th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
          th { background-color: #f2f2f2; font-weight: bold; }
          .total { text-align: right; font-size: 1.3em; font-weight: bold; margin-top: 20px; padding-top: 10px; border-top: 2px solid #333; }
          @media print {
            body { padding: 10px; }
            button { display: none; }
          }
        </style>
      </head>
      <body>
        <div class="header">
          <h1>BOLETA ELECTRÓNICA</h1>
          <p><strong>Tienda Gamer</strong></p>
        </div>
        <div class="info">
          <div class="info-row">
            <span><strong>N° Boleta:</strong></span>
            <span>${boleta.numero}</span>
          </div>
          <div class="info-row">
            <span><strong>Fecha:</strong></span>
            <span>${new Date(boleta.fechaEmision).toLocaleString('es-CL')}</span>
          </div>
          <div class="info-row">
            <span><strong>Cliente:</strong></span>
            <span>${boleta.nombreCliente}</span>
          </div>
          <div class="info-row">
            <span><strong>Email:</strong></span>
            <span>${boleta.emailCliente}</span>
          </div>
          ${boleta.direccionEnvio ? `
          <div class="info-row">
            <span><strong>Dirección:</strong></span>
            <span>${boleta.direccionEnvio}</span>
          </div>
          ` : ''}
          <div class="info-row">
            <span><strong>Método de Pago:</strong></span>
            <span>${boleta.metodoPago}</span>
          </div>
        </div>
        <div class="productos">
          <h3>Detalle de Productos</h3>
          ${productosHtml}
        </div>
        <div class="total">
          TOTAL: $${boleta.total?.toLocaleString('es-CL') || '0'}
        </div>
        <div style="text-align: center; margin-top: 30px;">
          <button onclick="window.print()" style="padding: 10px 20px; font-size: 16px; cursor: pointer;">
            Imprimir
          </button>
        </div>
      </body>
      </html>
    `;

    const ventana = window.open('', '_blank');
    if (ventana) {
      ventana.document.write(contenido);
      ventana.document.close();
    } else {
      alert('Por favor, permite las ventanas emergentes para imprimir la boleta');
    }
  };

  // Si no hay usuario, mostrar mensaje
  if (!usuario) {
    return (
      <div className="container py-5">
        <div className="alert alert-warning d-flex align-items-center" role="alert">
          <i className="bi bi-exclamation-triangle-fill me-3" style={{ fontSize: '1.5rem' }}></i>
          <div>
            <h5>Inicia sesión para ver tus boletas</h5>
            <p className="mb-0">Debes estar autenticado para acceder a este contenido.</p>
          </div>
        </div>
      </div>
    );
  }

  if (loading) {
    return (
      <div className="container py-5 text-center">
        <div className="spinner-border text-primary" role="status" style={{ width: '3rem', height: '3rem' }}>
          <span className="visually-hidden">Cargando...</span>
        </div>
        <p className="mt-3 text-muted">Cargando tus boletas...</p>
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
          <button className="btn btn-outline-danger" onClick={recargarBoletas} disabled={recargando}>
            {recargando ? (
              <>
                <span className="spinner-border spinner-border-sm me-2"></span>
                Recargando...
              </>
            ) : (
              <>
                <i className="bi bi-arrow-clockwise"></i> Reintentar
              </>
            )}
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
              <i className="bi bi-receipt"></i> Mis Boletas
            </h1>
            <p className="lead text-muted mb-0">
              {usuario.nombre && `Bienvenido/a ${usuario.nombre} - `}
              Historial de compras y boletas electrónicas
            </p>
          </div>
          <button 
            className="btn btn-outline-primary btn-lg" 
            onClick={recargarBoletas}
            disabled={recargando}
            title="Recargar boletas"
          >
            {recargando ? (
              <>
                <span className="spinner-border spinner-border-sm me-2"></span>
                Recargando...
              </>
            ) : (
              <>
                <i className="bi bi-arrow-clockwise"></i> Recargar
              </>
            )}
          </button>
        </div>

        {recargando && (
          <div className="alert alert-info d-flex align-items-center mb-4" role="alert">
            <span className="spinner-border spinner-border-sm me-3"></span>
            <span>Actualizando tus boletas...</span>
          </div>
        )}

        {boletas.length === 0 ? (
          <div className="card shadow-sm">
            <div className="card-body text-center py-5">
              <i className="bi bi-inbox" style={{ fontSize: '4rem', color: '#dee2e6' }}></i>
              <h4 className="mt-3 text-muted">No tienes boletas registradas</h4>
              <p className="text-muted mb-4">
                Cuando realices una compra, tus boletas aparecerán aquí automáticamente.
              </p>
              <div className="d-flex gap-2 justify-content-center">
                <button className="btn btn-primary" onClick={recargarBoletas} disabled={recargando}>
                  {recargando ? (
                    <>
                      <span className="spinner-border spinner-border-sm me-2"></span>
                      Recargando...
                    </>
                  ) : (
                    <>
                      <i className="bi bi-arrow-clockwise"></i> Verificar nuevamente
                    </>
                  )}
                </button>
                <a href="/" className="btn btn-outline-primary">
                  <i className="bi bi-shop"></i> Ir a la tienda
                </a>
              </div>
            </div>
          </div>
        ) : (
          <>
            <div className="alert alert-success d-flex align-items-center mb-4">
              <i className="bi bi-check-circle-fill me-3"></i>
              <span>
                Se encontraron <strong>{boletas.length}</strong> boleta{boletas.length !== 1 ? 's' : ''} 
                {usuario.nombre && ` para ${usuario.nombre}`}
              </span>
            </div>
            
            <div className="row">
              {boletas.map((boleta) => (
                <div key={boleta.id} className="col-md-6 col-lg-4 mb-4">
                  <div className="card shadow-sm h-100 hover-card">
                    <div className="card-header bg-primary text-white">
                      <h5 className="mb-0">
                        <i className="bi bi-file-earmark-text"></i> {boleta.numero}
                      </h5>
                    </div>
                    <div className="card-body">
                      <p className="mb-2">
                        <strong><i className="bi bi-calendar3"></i> Fecha:</strong>{' '}
                        {new Date(boleta.fechaEmision).toLocaleDateString('es-CL', {
                          year: 'numeric',
                          month: 'long',
                          day: 'numeric',
                          hour: '2-digit',
                          minute: '2-digit'
                        })}
                      </p>
                      <p className="mb-2">
                        <strong><i className="bi bi-person"></i> Cliente:</strong> {boleta.nombreCliente}
                      </p>
                      <p className="mb-2">
                        <strong><i className="bi bi-cash-coin"></i> Total:</strong>{' '}
                        <span className="text-success fw-bold fs-5">
                          ${boleta.total?.toLocaleString('es-CL') || '0'}
                        </span>
                      </p>
                      <p className="mb-2">
                        <span className={`badge ${boleta.estado === 'EMITIDA' ? 'bg-success' : 'bg-warning'} px-3 py-2`}>
                          {boleta.estado || 'EMITIDA'}
                        </span>
                      </p>
                    </div>
                    <div className="card-footer bg-transparent">
                      <div className="btn-group w-100" role="group">
                        <button
                          className="btn btn-outline-primary"
                          onClick={() => verDetalle(boleta)}
                          data-bs-toggle="modal"
                          data-bs-target="#modalDetalle"
                        >
                          <i className="bi bi-eye"></i> Ver
                        </button>
                        <button
                          className="btn btn-outline-success"
                          onClick={() => descargarBoleta(boleta)}
                        >
                          <i className="bi bi-printer"></i> Imprimir
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </>
        )}

        {/* Modal de Detalle */}
        <div className="modal fade" id="modalDetalle" tabIndex="-1">
          <div className="modal-dialog modal-lg">
            <div className="modal-content">
              <div className="modal-header bg-primary text-white">
                <h5 className="modal-title">
                  <i className="bi bi-file-earmark-text"></i> Detalle de Boleta
                </h5>
                <button type="button" className="btn-close btn-close-white" data-bs-dismiss="modal"></button>
              </div>
              <div className="modal-body">
                {boletaSeleccionada && (
                  <div>
                    <div className="row mb-3">
                      <div className="col-md-6">
                        <p><strong>N° Boleta:</strong> {boletaSeleccionada.numero}</p>
                        <p><strong>Fecha:</strong> {new Date(boletaSeleccionada.fechaEmision).toLocaleString('es-CL')}</p>
                        <p><strong>Estado:</strong> <span className="badge bg-success">{boletaSeleccionada.estado || 'EMITIDA'}</span></p>
                      </div>
                      <div className="col-md-6">
                        <p><strong>Cliente:</strong> {boletaSeleccionada.nombreCliente}</p>
                        <p><strong>Email:</strong> {boletaSeleccionada.emailCliente}</p>
                        <p><strong>Método de Pago:</strong> {boletaSeleccionada.metodoPago}</p>
                      </div>
                    </div>

                    {boletaSeleccionada.direccionEnvio && (
                      <div className="alert alert-info">
                        <strong>Dirección de Envío:</strong> {boletaSeleccionada.direccionEnvio}
                      </div>
                    )}

                    {boletaSeleccionada.detalleProductos && (
                      <div className="table-responsive">
                        <h6>Productos:</h6>
                        <table className="table table-striped">
                          <thead>
                            <tr>
                              <th>Producto</th>
                              <th>Cantidad</th>
                              <th>Precio Unit.</th>
                              <th>Subtotal</th>
                            </tr>
                          </thead>
                          <tbody>
                            {JSON.parse(boletaSeleccionada.detalleProductos).map((item, index) => (
                              <tr key={index}>
                                <td>Producto ID {item.productoId}</td>
                                <td>{item.cantidad}</td>
                                <td>${item.precioUnitario?.toLocaleString('es-CL') || '0'}</td>
                                <td>${item.subtotal?.toLocaleString('es-CL') || '0'}</td>
                              </tr>
                            ))}
                          </tbody>
                        </table>
                      </div>
                    )}

                    <div className="text-end mt-3">
                      <h4 className="text-primary">
                        Total: ${boletaSeleccionada.total?.toLocaleString('es-CL') || '0'}
                      </h4>
                    </div>
                  </div>
                )}
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">
                  Cerrar
                </button>
                <button
                  type="button"
                  className="btn btn-success"
                  onClick={() => descargarBoleta(boletaSeleccionada)}
                >
                  <i className="bi bi-printer"></i> Imprimir
                </button>
              </div>
            </div>
          </div>
        </div>

        <style>{`
          .hover-card {
            transition: transform 0.3s ease, box-shadow 0.3s ease;
          }
          .hover-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 0.5rem 1rem rgba(0,0,0,0.15) !important;
          }
        `}</style>
      </div>
    </div>
  );
}
import React, { useEffect, useState } from 'react';
import { productoService } from '../API/productoService';
import { useCarrito } from '../context/CarritoContext';

export default function Catalogo() {
  const { agregarAlCarrito } = useCarrito();
  const [productos, setProductos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [busqueda, setBusqueda] = useState('');

  useEffect(() => {
    cargarProductos();
  }, []);

  const cargarProductos = async () => {
    try {
      const data = await productoService.obtenerTodos();
      setProductos(data);
    } finally {
      setLoading(false);
    }
  };

  const productosFiltrados = productos.filter((producto) =>
    producto.nombre.toLowerCase().includes(busqueda.toLowerCase())
  );

  return (
    <div className="bg-light min-vh-100 py-4">
      <div className="container" style={{ maxWidth: '900px' }}>

        <h2 className="fw-bold text-primary mb-4 text-center">Catálogo de Productos</h2>

        <input
          className="form-control form-control-lg mb-4"
          placeholder="Buscar productos..."
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />

        {loading ? (
          <p className="text-center text-muted">Cargando...</p>
        ) : (
          <div style={{ maxHeight: '70vh', overflowY: 'auto' }} className="pe-2">
            <div className="list-group shadow-sm">
              {productosFiltrados.map((producto) => (
                <div
                  key={producto.id}
                  className="list-group-item list-group-item-action d-flex align-items-center py-3"
                >
                  {/* Mini header con logo */}
                  <div className="me-3 p-3 rounded" style={{ background: '#f1f3f5' }}>
                    <img
                      src="/logo.png"
                      alt="Logo"
                      style={{ width: '55px', height: 'auto' }}
                    />
                  </div>

                  {/* Contenido */}
                  <div className="flex-grow-1">
                    <h5 className="mb-1 fw-bold">{producto.nombre}</h5>
                    <p className="text-muted small mb-1">
                      {producto.descripcion || 'Sin descripción'}
                    </p>
                    <span className="fw-bold text-primary">
                      ${producto.precio?.toLocaleString('es-CL')}
                    </span>
                  </div>

                  {/* Botón */}
                  <button
                    className="btn btn-primary btn-lg"
                    onClick={() => agregarAlCarrito(producto)}
                  >
                    Agregar
                  </button>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

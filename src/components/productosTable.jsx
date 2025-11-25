import React from "react";

const ProductosTable = ({ data }) => {
  const productos = Array.isArray(data) ? data : [];

  if (productos.length === 0) {
    return (
      <div className="text-muted text-center py-4">
        No hay productos disponibles
      </div>
    );
  }

  return (
    <div className="table-responsive">
      <table className="table table-sm">
        <thead>
          <tr className="border-bottom">
            <th className="text-muted fw-normal">ID</th>
            <th className="text-muted fw-normal">Nombre</th>
            <th className="text-muted fw-normal">Categoría</th>
            <th className="text-muted fw-normal text-end">Precio</th>
            <th className="text-muted fw-normal text-center">Stock</th>
          </tr>
        </thead>
        <tbody>
          {productos.map((producto) => (
            <tr key={producto.id} className="border-bottom">
              <td className="text-muted">#{producto.id}</td>
              <td>{producto.nombre || 'N/A'}</td>
              <td className="text-muted">{producto.categoria || 'N/A'}</td>
              <td className="text-end fw-semibold">
                ${producto.precio ? producto.precio.toLocaleString('es-CL') : '0'}
              </td>
              <td className="text-center">{producto.stock || 0}</td>
            </tr>
          ))}
        </tbody>
      </table>
      
      <div className="text-end text-muted small mt-2">
        {productos.length} productos
      </div>
    </div>
  );
};

export default ProductosTable;
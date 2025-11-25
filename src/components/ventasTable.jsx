import React from "react";

const VentasTable = ({ data }) => {
  const ventas = Array.isArray(data) ? data : [];

  if (ventas.length === 0) {
    return (
      <div className="text-muted text-center py-4">
        No hay ventas disponibles
      </div>
    );
  }

  return (
    <div className="table-responsive">
      <table className="table table-sm">
        <thead>
          <tr className="border-bottom">
            <th className="text-muted fw-normal">ID</th>
            <th className="text-muted fw-normal">Cliente</th>
            <th className="text-muted fw-normal">Email</th>
            <th className="text-muted fw-normal">Fecha</th>
            <th className="text-muted fw-normal text-center">Productos</th>
            <th className="text-muted fw-normal text-center">Estado</th>
            <th className="text-muted fw-normal">Método Pago</th>
            <th className="text-muted fw-normal text-end">Total</th>
          </tr>
        </thead>
        <tbody>
          {ventas.map((venta) => (
            <tr key={venta.id} className="border-bottom">
              <td className="text-muted">#{venta.id}</td>
              <td>{venta.nombreCliente} {venta.apellidoCliente}</td>
              <td className="text-muted small">{venta.emailCliente || 'N/A'}</td>
              <td className="text-muted">
                {venta.fechaVenta 
                  ? new Date(venta.fechaVenta).toLocaleDateString('es-ES', {
                    })
                  : 'N/A'}
              </td>
              <td className="text-center">
                {venta.productos?.length || 0}
              </td>
              <td className="text-center">
                <span className={`badge ${
                  venta.estado === 'COMPLETADA' ? 'bg-success' : 
                  venta.estado === 'PENDIENTE' ? 'bg-warning' : 
                  'bg-secondary'
                }`} style={{ fontSize: '0.7rem' }}>
                  {venta.estado || 'N/A'}
                </span>
              </td>
              <td className="text-muted">{venta.metodoPago || 'N/A'}</td>
              <td className="text-end fw-semibold">
                ${venta.total ? venta.total.toLocaleString('es-CL') : '0'}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      
      <div className="d-flex justify-content-between text-muted small mt-2">
        <span>{ventas.length} ventas</span>
        <span className="fw-semibold">
          Total: ${ventas.reduce((sum, v) => sum + (v.total || 0), 0).toLocaleString('es-CL')}
        </span>
      </div>
    </div>
  );
};

export default VentasTable;
import React from "react";

const BoletasTable = ({ data }) => {
  // Verificar si data es un array
  const boletas = Array.isArray(data) ? data : [];

  if (boletas.length === 0) {
    return (
      <div className="text-muted text-center py-4">
        No hay boletas disponibles
      </div>
    );
  }

  return (
    <div className="table-responsive">
      <table className="table table-sm">
        <thead>
          <tr className="border-bottom">
            <th className="text-muted fw-normal">ID</th>
            <th className="text-muted fw-normal">Email Cliente</th>
            <th className="text-muted fw-normal">Numero Boleta</th>
            <th className="text-muted fw-normal">Fecha</th>
            <th className="text-muted fw-normal text-end">Total</th>
          </tr>
        </thead>
        <tbody>
          {boletas.map((boleta) => (
            <tr key={boleta.id} className="border-bottom">
              <td className="text-muted">#{boleta.id}</td>
              <td>{boleta.emailCliente}</td>
              <td>{boleta.numero}</td>
              <td className="text-muted">
                {boleta.fechaEmision 
                  ? new Date(boleta.fechaEmision).toLocaleDateString('es-ES')
                  : 'N/A'}
              </td>
              <td className="text-end fw-semibold">
                ${boleta.total ? boleta.total.toLocaleString('es-CL') : '0'}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      
      <div className="text-end text-muted small mt-2">
        {boletas.length} boletas
      </div>
    </div>
  );
};

export default BoletasTable;
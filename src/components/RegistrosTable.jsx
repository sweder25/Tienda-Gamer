import React from "react";

const RegistrosTable = ({ data }) => {
  const registros = Array.isArray(data) ? data : [];

  if (registros.length === 0) {
    return (
      <div className="text-muted text-center py-4">
        No hay registros disponibles
      </div>
    );
  }

  return (
    <div className="table-responsive">
      <table className="table table-sm">
        <thead>
          <tr className="border-bottom">
            <th className="text-muted fw-normal">ID</th>
            <th className="text-muted fw-normal">ROL</th>
            <th className="text-muted fw-normal">Usuario</th>
            <th className="text-muted fw-normal">Email</th>
            <th className="text-muted fw-normal">Direccion</th>
          </tr>
        </thead>
        <tbody>
          {registros.map((registro) => (
            <tr key={registro.id} className="border-bottom">
              <td className="text-muted">#{registro.id}</td>
              <td>{registro.rol}</td>
              <td className="text-muted">{registro.nombre || 'N/A'}</td>
              <td className="text-muted">
                {registro.email}
              </td>
              <td className="text-muted">{registro.direccion || 'N/A'}</td>
            </tr>
          ))}
        </tbody>
      </table>
      
      <div className="text-end text-muted small mt-2">
        {registros.length} registros
      </div>
    </div>
  );
};

export default RegistrosTable;
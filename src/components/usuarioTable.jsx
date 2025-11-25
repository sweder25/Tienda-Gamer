import React from "react";

const UsuariosTable = ({ data }) => {
  const usuarios = Array.isArray(data) ? data : [];

  if (usuarios.length === 0) {
    return (
      <div className="text-muted text-center py-4">
        No hay usuarios disponibles
      </div>
    );
  }

  return (
    <div className="table-responsive">
      <table className="table table-sm">
        <thead>
          <tr className="border-bottom">
            <th className="text-muted fw-normal">ID</th>
            <th className="text-muted fw-normal">Email</th>
            <th className="text-muted fw-normal">Password</th>
          </tr>
        </thead>
        <tbody>
          {usuarios.map((usuario) => (
            <tr key={usuario.id} className="border-bottom">
              <td className="text-muted">#{usuario.id}</td>
              <td className="text-muted">{usuario.email || 'N/A'}</td>
              <td className="text-muted">{usuario.password || 'N/A'}</td>
            </tr>
          ))}
        </tbody>
      </table>
      
      <div className="text-end text-muted small mt-2">
        {usuarios.length} usuarios
      </div>
    </div>
  );
};

export default UsuariosTable;
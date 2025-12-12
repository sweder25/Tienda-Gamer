import { render, screen } from "@testing-library/react";
import UsuariosTable from "../components/usuarioTable";
import React from "react";

describe("UsuariosTable Component", () => {
  const usuariosMock = [
    { id: 1, email: "maxi@test.com", password: "secret1" },
    { id: 2, email: "juan@test.com", password: "secret2" },
  ];

  test("muestra encabezados de tabla", () => {
    render(<UsuariosTable data={usuariosMock} />);

    expect(screen.getByText("ID")).toBeInTheDocument();
    expect(screen.getByText("Email")).toBeInTheDocument();
    expect(screen.getByText("Password")).toBeInTheDocument();
  });

  test("renderiza usuarios correctamente", () => {
    render(<UsuariosTable data={usuariosMock} />);

    expect(screen.getByText("#1")).toBeInTheDocument();
    expect(screen.getByText("maxi@test.com")).toBeInTheDocument();
    expect(screen.getByText("secret1")).toBeInTheDocument();
  });

  test("muestra mensaje si no hay usuarios", () => {
    render(<UsuariosTable data={[]} />);

    expect(screen.getByText("No hay usuarios disponibles")).toBeInTheDocument();
  });
});

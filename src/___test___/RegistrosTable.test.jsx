import { render, screen } from "@testing-library/react";
import RegistrosTable from "../components/RegistrosTable";
import React from "react";

describe("RegistrosTable Component", () => {
  const registrosMock = [
    { id: 1, rol: "ADMIN", nombre: "Maxi", email: "maxi@example.com", direccion: "Calle 123" },
  ];

  test("muestra encabezados de tabla", () => {
    render(<RegistrosTable data={registrosMock} />);

    expect(screen.getByText("ID")).toBeInTheDocument();
    expect(screen.getByText("ROL")).toBeInTheDocument();
    expect(screen.getByText("Usuario")).toBeInTheDocument();
    expect(screen.getByText("Email")).toBeInTheDocument();
    expect(screen.getByText("Direccion")).toBeInTheDocument();
  });

  test("renderiza registros correctamente", () => {
    render(<RegistrosTable data={registrosMock} />);

    expect(screen.getByText("#1")).toBeInTheDocument();
    expect(screen.getByText("ADMIN")).toBeInTheDocument();
    expect(screen.getByText("Maxi")).toBeInTheDocument();
    expect(screen.getByText("maxi@example.com")).toBeInTheDocument();
    expect(screen.getByText("Calle 123")).toBeInTheDocument();
  });

  test("muestra mensaje si no hay registros", () => {
    render(<RegistrosTable data={[]} />);

    expect(screen.getByText("No hay registros disponibles")).toBeInTheDocument();
  });
});

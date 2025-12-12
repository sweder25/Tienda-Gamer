import { render, screen } from "@testing-library/react";
import BoletasTable from "../components/boletasTable";
import React from "react";    

describe("BoletasTable Component", () => {
  const boletasMock = [
    {
      id: 10,
      emailCliente: "maxi@example.com",
      numero: "BOL-00010",
      fechaEmision: "2024-05-16T00:00:00.000Z",
      total: 25990,
    },
  ];

  test("muestra encabezados de tabla", () => {
    render(<BoletasTable data={boletasMock} />);

    expect(screen.getByText("ID")).toBeInTheDocument();
    expect(screen.getByText("Email Cliente")).toBeInTheDocument();
    expect(screen.getByText("Numero Boleta")).toBeInTheDocument();
    expect(screen.getByText("Fecha")).toBeInTheDocument();
    expect(screen.getByText("Total")).toBeInTheDocument();
  });

  test("renderiza una boleta correctamente", () => {
    render(<BoletasTable data={boletasMock} />);

    // ID con prefijo # en la celda
    expect(screen.getByText("#10")).toBeInTheDocument();
    expect(screen.getByText("maxi@example.com")).toBeInTheDocument();
    expect(screen.getByText("BOL-00010")).toBeInTheDocument();
    expect(screen.getByText("25.990", { exact: false })).toBeInTheDocument();
  });

  test("muestra mensaje si no hay boletas", () => {
    render(<BoletasTable data={[]} />);

    expect(screen.getByText("No hay boletas disponibles")).toBeInTheDocument();
  });
});

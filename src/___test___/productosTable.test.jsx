import { render, screen } from "@testing-library/react";
import ProductosTable from "../components/productosTable";
import React from "react";

describe("ProductosTable Component", () => {
  const productosMock = [
     { id: 1, nombre: "Laptop Gamer", precio: 1200, categoria: "Tecnología" },
     { id: 2, nombre: "Mouse RGB", precio: 25, categoria: "Accesorios" },
  ];

  test("muestra los encabezados de la tabla", () => {
     render(<ProductosTable data={productosMock} />);

    expect(screen.getByText("ID")).toBeInTheDocument();
    expect(screen.getByText("Nombre")).toBeInTheDocument();
    expect(screen.getByText("Precio")).toBeInTheDocument();
     // El componente usa "Categoría" con tilde
     expect(screen.getByText("Categoría")).toBeInTheDocument();
  });

  test("renderiza correctamente los productos", () => {
     render(<ProductosTable data={productosMock} />);

    expect(screen.getByText("Laptop Gamer")).toBeInTheDocument();
    expect(screen.getByText("Mouse RGB")).toBeInTheDocument();
  });

  test("muestra mensaje cuando la lista está vacía", () => {
     render(<ProductosTable data={[]} />);
    expect(screen.getByText("No hay productos disponibles")).toBeInTheDocument();
  });
});

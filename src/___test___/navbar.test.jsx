import { render, screen, fireEvent } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import Navbar from "../components/navbar";
import React from "react";

// Mock del contexto de autenticación
jest.mock("../context/AuthContext", () => ({
  useAuth: () => ({
    usuario: { nombre: "Maximiliano el guapo" },
    isAuthenticated: true,
    logout: jest.fn(),
  }),
}));

// Mock del useNavigate
const mockNavigate = jest.fn();
jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useNavigate: () => mockNavigate,
}));

describe("Navbar Component", () => {
  test("muestra el nombre del usuario autenticado", () => {
    render(
      <BrowserRouter>
        <Navbar />
      </BrowserRouter>
    );

    // El componente muestra el nombre completo: "Maximiliano el guapo"
    expect(screen.getByText(/Maximiliano\s+el\s+guapo/i)).toBeInTheDocument();
  });

  test("muestra botón de cerrar sesión cuando está autenticado", () => {
    render(
      <BrowserRouter>
        <Navbar />
      </BrowserRouter>
    );

    expect(screen.getByText("Cerrar Sesión")).toBeInTheDocument();
  });

  test("ejecuta logout y navega al hacer clic en Cerrar Sesión", () => {
    render(
      <BrowserRouter>
        <Navbar />
      </BrowserRouter>
    );

    const button = screen.getByText("Cerrar Sesión");
    fireEvent.click(button);

    // Se debe llamar navigate('/inicio') (puede incluir opciones como { replace: true })
    const calls = mockNavigate.mock.calls;
    expect(calls.length).toBeGreaterThan(0);
    expect(calls[0][0]).toBe("/inicio");
  });
});

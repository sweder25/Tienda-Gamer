import { render, screen, act } from "@testing-library/react";
import { AuthProvider, useAuth } from "../context/AuthContext";
import React from "react";
import { MemoryRouter } from "react-router-dom";

// Mock del servicio de ingreso usado por AuthContext
jest.mock("../API/IngresoService", () => ({
  ingresoService: {
    obtenerUsuarioActual: jest.fn(() => null),
    login: jest.fn(async (credenciales) => ({ usuario: { email: credenciales.email, id: 99 } })),
    logout: jest.fn(() => {}),
  },
}));


// Componente utilitario para probar el contexto
function TestComponent() {
  const { usuario, isAuthenticated, login, logout } = useAuth();

  return (
    <div>
      <p data-testid="auth">{isAuthenticated ? "YES" : "NO"}</p>
      <p data-testid="user">{usuario ? usuario.email : "null"}</p>
      <button onClick={() => login({ email: "test@test.com" }, "TOKEN123")}>
        login
      </button>
      <button onClick={logout}>logout</button>
    </div>
  );
}

describe("AuthContext Tests", () => {

  beforeEach(() => {
    localStorage.clear();
    const { ingresoService } = require("../API/IngresoService");
    ingresoService.obtenerUsuarioActual.mockReturnValue(null);
  });

  test("por defecto debe iniciar sin usuario autenticado", () => {
    render(
      <MemoryRouter>
        <AuthProvider>
          <TestComponent />
        </AuthProvider>
      </MemoryRouter>
    );

    expect(screen.getByTestId("auth").textContent).toBe("NO");
    expect(screen.getByTestId("user").textContent).toBe("null");
  });

  test("login debe establecer usuario y marcar autenticación", async () => {
    const { ingresoService } = require("../API/IngresoService");
    render(
      <MemoryRouter>
        <AuthProvider>
          <TestComponent />
        </AuthProvider>
      </MemoryRouter>
    );

    await act(async () => {
      screen.getByText("login").click();
    });

    // Se actualiza el contexto con el usuario devuelto por el servicio
    expect(ingresoService.login).toHaveBeenCalledWith({ email: "test@test.com" });
    expect(screen.getByTestId("auth").textContent).toBe("YES");
    expect(screen.getByTestId("user").textContent).toBe("test@test.com");
  });

  test("logout debe limpiar usuario y autenticación", async () => {
    const { ingresoService } = require("../API/IngresoService");
    render(
      <MemoryRouter>
        <AuthProvider>
          <TestComponent />
        </AuthProvider>
      </MemoryRouter>
    );

    // Primero login
    await act(async () => {
      screen.getByText("login").click();
    });

    // Luego logout
    await act(async () => {
      screen.getByText("logout").click();
    });

    expect(ingresoService.logout).toHaveBeenCalled();
    expect(screen.getByTestId("auth").textContent).toBe("NO");
    expect(screen.getByTestId("user").textContent).toBe("null");
  });

  test("si obtenerUsuarioActual devuelve datos debe iniciar autenticado", () => {
    const { ingresoService } = require("../API/IngresoService");
    ingresoService.obtenerUsuarioActual.mockReturnValue({ email: "saved@test.com", id: 77 });

    render(
      <MemoryRouter>
        <AuthProvider>
          <TestComponent />
        </AuthProvider>
      </MemoryRouter>
    );

    expect(screen.getByTestId("auth").textContent).toBe("YES");
    expect(screen.getByTestId("user").textContent).toBe("saved@test.com");
  });
});

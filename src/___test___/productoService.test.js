import { productoService } from "../API/productoService";
import React from "react";


//para no mostrar errores en consola durante los tests
const originalError = console.error;
beforeAll(() => { console.error = jest.fn(); });
afterAll(() => { console.error = originalError; });


// mock fetch global
global.fetch = jest.fn();

describe("productoService API tests", () => {

  beforeEach(() => {
    fetch.mockClear();
  });
  // obtenerTodos()
  test("obtenerTodos → retorna lista de productos cuando la API responde OK", async () => {
    const mockResponse = { data: [{ id: 1, nombre: "Producto A" }] };

    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    });

    const result = await productoService.obtenerTodos();

    expect(fetch).toHaveBeenCalledWith("http://localhost:8083/api/productos");
    expect(result).toEqual(mockResponse.data);
  });

  test("obtenerTodos → retorna [] cuando la API falla", async () => {
    fetch.mockRejectedValueOnce(new Error("Network Error"));

    const result = await productoService.obtenerTodos();

    expect(result).toEqual([]); 
  });

  // obtenerPorId()
  test("obtenerPorId → retorna producto por ID", async () => {
    const mockResponse = { data: { id: 5, nombre: "Mouse Gamer" } };

    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    });

    const result = await productoService.obtenerPorId(5);

    expect(fetch).toHaveBeenCalledWith("http://localhost:8083/api/productos/5");
    expect(result).toEqual(mockResponse.data);
  });

  test("obtenerPorId → lanza error si el producto no existe", async () => {
    fetch.mockResolvedValueOnce({
      ok: false
    });

    await expect(productoService.obtenerPorId(999))
      .rejects
      .toThrow("Producto no encontrado");
  });

  // crear()
  test("crear → realiza POST con el body correcto", async () => {
    const nuevo = { nombre: "Teclado RGB", precio: 29990 };
    const mockResponse = { data: { id: 10, ...nuevo } };

    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    });

    const result = await productoService.crear(nuevo);

    expect(fetch).toHaveBeenCalledWith("http://localhost:8083/api/productos", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(nuevo)
    });

    expect(result).toEqual(mockResponse.data);
  });

  test("crear → lanza error si la API responde mal", async () => {
    fetch.mockResolvedValueOnce({
      ok: false
    });

    await expect(productoService.crear({ nombre: "X" }))
      .rejects
      .toThrow("Error al crear producto");
  });
  // actualizar()
  test("actualizar → realiza PUT correctamente", async () => {
    const update = { nombre: "Mouse actualizado" };
    const mockResponse = { data: { id: 3, ...update } };

    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    });

    const result = await productoService.actualizar(3, update);

    expect(fetch).toHaveBeenCalledWith("http://localhost:8083/api/productos/3", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(update)
    });

    expect(result).toEqual(mockResponse.data);
  });

  test("actualizar → lanza error si falla el servidor", async () => {
    fetch.mockResolvedValueOnce({ ok: false });

    await expect(productoService.actualizar(99, {}))
      .rejects
      .toThrow("Error al actualizar producto");
  });

  // eliminar()
  test("eliminar → realiza DELETE y retorna true", async () => {
    fetch.mockResolvedValueOnce({
      ok: true
    });

    const result = await productoService.eliminar(8);

    expect(fetch).toHaveBeenCalledWith("http://localhost:8083/api/productos/8", {
      method: "DELETE"
    });

    expect(result).toBe(true);
  });

  test("eliminar → lanza error si la API responde mal", async () => {
    fetch.mockResolvedValueOnce({ ok: false });

    await expect(productoService.eliminar(123))
      .rejects
      .toThrow("Error al eliminar producto");
  });

});

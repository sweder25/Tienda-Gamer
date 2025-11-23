package com.apiProductos.productos.controller;

import com.apiProductos.productos.model.Producto;
import com.apiProductos.productos.services.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "API para gestión de productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping("/crear")
    @Operation(summary = "Crear nuevo producto")
    public ResponseEntity<Map<String, Object>> crearProducto(@RequestBody Producto producto) {
        Map<String, Object> response = new HashMap<>();
        try {
            Producto nuevo = productoService.crearProducto(producto);
            response.put("success", true);
            response.put("message", "Producto creado exitosamente");
            response.put("data", nuevo);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Producto producto = productoService.obtenerPorId(id);
            response.put("success", true);
            response.put("data", producto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todos los productos")
    public ResponseEntity<Map<String, Object>> listarTodos() {
        Map<String, Object> response = new HashMap<>();
        List<Producto> productos = productoService.listarTodos();
        response.put("success", true);
        response.put("data", productos);
        response.put("total", productos.size());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/actualizar/{id}")
    @Operation(summary = "Actualizar producto")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        Map<String, Object> response = new HashMap<>();
        try {
            Producto actualizado = productoService.actualizar(id, producto);
            response.put("success", true);
            response.put("message", "Producto actualizado exitosamente");
            response.put("data", actualizado);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar producto")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            productoService.eliminar(id);
            response.put("success", true);
            response.put("message", "Producto eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
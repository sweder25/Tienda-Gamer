package com.apiProductos.productos.controller;

import com.apiProductos.productos.model.Producto;
import com.apiProductos.productos.services.ProductoService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarTodos() {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("GET /api/productos");
            List<Producto> productos = productoService.listarTodos();
            response.put("success", true);
            response.put("data", productos);
            response.put("total", productos.size());
            System.out.println("Productos encontrados: " + productos.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID" , description = "Obtiene un producto específico usando su ID"    )
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    

    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto en el sistema")
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Producto producto) {
        Map<String, Object> response = new HashMap<>();
            return ResponseEntity.badRequest().body(response);
        }
    

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        Map<String, Object> response = new HashMap<>();
        try {
            Producto actualizado = productoService.actualizar(id, producto);
            response.put("success", true);
            response.put("message", "Producto actualizado");
            response.put("data", actualizado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            productoService.eliminar(id);
            response.put("success", true);
            response.put("message", "Producto eliminado");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}
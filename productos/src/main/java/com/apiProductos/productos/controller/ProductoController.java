package com.apiProductos.productos.controller;

import com.apiProductos.productos.model.Producto;
import com.apiProductos.productos.service.ProductoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Producto> listarProductos() {
        return service.listarProductos();
    }

    @PostMapping("/categoria/{categoriaId}")
    public Producto crearProducto(@RequestBody Producto producto, @PathVariable Long categoriaId) {
        return service.crearProducto(producto, categoriaId);
    }

    @GetMapping("/categoria/{nombreCategoria}")
    public List<Producto> listarPorCategoria(@PathVariable String nombreCategoria) {
        return service.buscarPorCategoria(nombreCategoria);
    }
}

package com.apiProductos.productos.controller;

import com.apiProductos.productos.model.Productos;
import com.apiProductos.productos.services.ProductoServices;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoServices service;

    public ProductoController(ProductoServices service) {
        this.service = service;
    }

    @GetMapping
    public List<Productos> listarProductos() {
        return service.listarProductos();
    }

    @PostMapping
    public Productos crearProducto(@RequestBody Productos producto) {
        return service.crearProducto(producto);
    }   


    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        service.eliminarProducto(id);
    }

    @PutMapping("/{id}")
    public void actualizarProducto(@PathVariable Long id, @RequestBody Productos productoActualizado)
    {
        service.actualizarProducto(id, productoActualizado);   
}

}
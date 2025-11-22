package com.apiProductos.productos.controller;

import com.apiProductos.productos.model.Productos;
import com.apiProductos.productos.services.ProductoServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/producto")
@Tag (name = "Productos", description = "Operaciones para gestionar productos del sistema")  
public class ProductoController {

    private final ProductoServices service;

    public ProductoController(ProductoServices service) {
        this.service = service;
    }

    @Operation(summary = "Obtener un producto por ID")
    @Description("Este endpoint permite obtener los detalles de un producto específico utilizando su ID.")
    @GetMapping("/{id}")
    public Productos obtenerProducto(@PathVariable Long id) {
        return service.obtenerProducto(id);
    }

    @Operation(summary = "Listar todos los productos")
    @Description("Este endpoint permite obtener una lista de todos los productos disponibles en el sistema.")   
    @GetMapping 
    public List<Productos> listarProductos() {
        return service.listarProductos();
    }

    @Operation(summary = "Crear un nuevo producto")
    @Description("Este endpoint permite crear un nuevo producto en el sistema.")    
    @PostMapping
    public Productos crearProducto(@RequestBody Productos producto) {
        return service.crearProducto(producto);
    }   

    @Operation(summary = "Eliminar un producto")
    @Description("Este endpoint permite eliminar un producto existente en el sistema.") 
    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        service.eliminarProducto(id);
    }

    @Operation(summary = "Actualizar un producto")
    @Description("Este endpoint permite actualizar la información de un producto existente en el sistema.")
    @PutMapping("/{id}")
    public void actualizarProducto(@PathVariable Long id, @RequestBody Productos productoActualizado)
    {
        service.actualizarProducto(id, productoActualizado);   
}

}
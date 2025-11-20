package com.apiProductos.productos.controller;

import com.apiProductos.productos.model.Categoria;
import com.apiProductos.productos.services.CategoriaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Categoria> listarCategorias() {
        return service.listarCategorias();
    }

    @PostMapping
    public Categoria crearCategoria(@RequestBody Categoria categoria) {
        return service.crearCategoria(categoria);
    }

    @GetMapping("/{id}")
    public Categoria buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }
}

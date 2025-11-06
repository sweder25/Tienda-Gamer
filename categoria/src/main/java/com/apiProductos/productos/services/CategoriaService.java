package com.apiProductos.productos.service;

import com.apiProductos.productos.model.Categoria;
import com.apiProductos.productos.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repo;

    public CategoriaService(CategoriaRepository repo) {
        this.repo = repo;
    }

    public List<Categoria> listarCategorias() {
        return repo.findAll();
    }

    public Categoria crearCategoria(Categoria categoria) {
        return repo.save(categoria);
    }

    public Categoria buscarPorId(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    public Categoria buscarPorNombre(String nombre) {
        return repo.findByNombre(nombre);
    }
}

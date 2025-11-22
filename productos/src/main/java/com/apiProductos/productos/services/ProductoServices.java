package com.apiProductos.productos.services;

import com.apiProductos.productos.model.Productos;
import com.apiProductos.productos.repository.ProductoRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServices {

    private final ProductoRepository repository;

    public ProductoServices(ProductoRepository repository) {
        this.repository = repository;
    }

    public Productos obtenerProducto(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Productos> listarProductos() {
        return repository.findAll();
    }

    public Productos crearProducto(Productos producto) {
        return repository.save(producto);
    }

    public void eliminarProducto(Long id) {
        repository.deleteById(id);
    }

    public void actualizarProducto(Long id, Productos productoActualizado) {
        if (repository.existsById(id)) {
            productoActualizado.setId(id);
            repository.save(productoActualizado);
        }
    }
}
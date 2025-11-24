package com.apiProductos.productos.services;

import com.apiProductos.productos.model.Producto;
import com.apiProductos.productos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public Producto obtenerPorId(Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            return producto.get();
        } else {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public List<Producto> listarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    public Producto crear(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto producto) {
        Producto existente = obtenerPorId(id);
        
        if (producto.getNombre() != null) {
            existente.setNombre(producto.getNombre());
        }
        if (producto.getDescripcion() != null) {
            existente.setDescripcion(producto.getDescripcion());
        }
        if (producto.getPrecio() != null) {
            existente.setPrecio(producto.getPrecio());
        }
        if (producto.getStock() != null) {
            existente.setStock(producto.getStock());
        }
        if (producto.getCategoria() != null) {
            existente.setCategoria(producto.getCategoria());
        }
        
        return productoRepository.save(existente);
    }

    public Producto actualizarStock(Long id, Integer nuevoStock) {
        Producto producto = obtenerPorId(id);
        
        if (nuevoStock < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }
        
        producto.setStock(nuevoStock);
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        Producto producto = obtenerPorId(id);
        productoRepository.delete(producto);
    }
}
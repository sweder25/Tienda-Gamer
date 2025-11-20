package com.apiProductos.productos.services;
import com.apiProductos.productos.model.Productos;
import com.apiProductos.productos.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoServices {

    private final ProductoRepository productoRepo;

    public ProductoServices(ProductoRepository productoRepo) {
        this.productoRepo = productoRepo;

    }

    public List<Productos> listarProductos() {
        return productoRepo.findAll();
    }

    @SuppressWarnings("null")
    public Productos crearProducto(Productos producto) {
        return productoRepo.save(producto);
    }

    public void eliminarProducto(Long id) {
        productoRepo.deleteById(id);
    }

    public void actualizarProducto(Long id, Productos productoActualizado) {
        Productos productoExistente = productoRepo.findById(id).orElse(null);
        if (productoExistente != null) {
            productoExistente.setNombreProducto(productoActualizado.getNombreProducto());
            productoExistente.setDescripcion(productoActualizado.getDescripcion());
            productoRepo.save(productoExistente);
        }
    }
}

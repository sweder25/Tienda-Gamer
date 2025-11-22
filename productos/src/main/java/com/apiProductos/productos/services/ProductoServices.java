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
        Productos productoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        productoExistente.setNombreProducto(productoActualizado.getNombreProducto());
        productoExistente.setDescripcion(productoActualizado.getDescripcion());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setCantidad(productoActualizado.getCantidad());
        productoExistente.setDigital(productoActualizado.getDigital());

        repository.save(productoExistente);
    }

    public Productos obtenerProducto(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));   
}
}
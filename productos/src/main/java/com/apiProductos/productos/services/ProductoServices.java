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

    // Overloaded method used by the controller when a categoriaId is provided.
    // Currently this implementation saves the product as-is. To associate the
    // Productos with a Categoria entity you'll need to add a dependency on the
    // `categoria` module and set producto.setCategoria(...) before saving.
    @SuppressWarnings("null")
    public Productos crearProducto(Productos producto, Long categoriaId) {
        // TODO: wire Categoria association when cross-module dependency is available
        return productoRepo.save(producto);
    }

    public List<Productos> buscarPorCategoria(String nombreCategoria) {
        return productoRepo.findByCategoriaNombre(nombreCategoria);
    }
}

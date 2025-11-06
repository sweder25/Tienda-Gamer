package com.apiProductos.productos.services;
import com.apiProductos.productos.model.Productos;
import com.apiProductos.productos.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductosServices {

    private final ProductoRepository productoRepo;

    public ProductosServices(ProductoRepository productoRepo) {
        this.productoRepo = productoRepo;

    }

    public List<Productos> listarProductos() {
        return productoRepo.findAll();
    }

    public Productos crearProducto(Productos producto) {
        return productoRepo.save(producto);
    }
}

package com.apiProductos.productos.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import com.apiProductos.productos.model.Producto;
import com.apiProductos.productos.repository.ProductoRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    public DataLoader(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }


    // Cargar datos iniciales solo si la tabla esta vacia, en caso de que le agregan 
    // productos, eliminar para que esto funcione.
    @Override
    public void run(String... args) throws Exception {
        if (productoRepository.count() == 0) {
            Producto p1 = new Producto();
            p1.setNombre("Teclado mecánico");
            p1.setDescripcion("RGB, switches marrones");
            p1.setPrecio(79.99);
            p1.setCategoria("Periféricos");
            p1.setStock(50);

            Producto p2 = new Producto();
            p2.setNombre("Mouse gamer");
            p2.setDescripcion("Óptico 16000 DPI, RGB");
            p2.setPrecio(49.99);
            p2.setCategoria("Periféricos");
            p2.setStock(100);

            Producto p3 = new Producto();
            p3.setNombre("Auriculares gaming");
            p3.setDescripcion("Sonido 7.1, micrófono desmontable");
            p3.setPrecio(89.99);
            p3.setCategoria("Audio");
            p3.setStock(30);

            productoRepository.saveAll(Arrays.asList(p1, p2, p3));
        }
    }
}

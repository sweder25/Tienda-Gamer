package com.apiProductos.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.apiProductos.productos.model.Producto;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoriaNombre(String nombreCategoria);
}

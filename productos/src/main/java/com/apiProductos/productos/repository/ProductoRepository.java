package com.apiProductos.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.apiProductos.productos.model.Productos;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Productos, Long> {
    List<Productos> findByNombreProducto(String nombreProducto);
}
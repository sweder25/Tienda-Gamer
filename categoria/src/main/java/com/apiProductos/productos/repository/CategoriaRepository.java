package com.apiProductos.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.apiProductos.productos.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Categoria findByNombre(String nombre);
}

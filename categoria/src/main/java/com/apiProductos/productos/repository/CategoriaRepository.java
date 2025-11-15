package com.apiProductos.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.apiProductos.productos.model.Categoria;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);
}

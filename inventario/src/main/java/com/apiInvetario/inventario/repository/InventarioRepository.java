package com.apiInvetario.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.apiInvetario.inventario.model.Inventario;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    Optional<Inventario> findByProductoId(Long productoId);
}

package com.apiProductos.productos.repository;

import com.apiProductos.productos.model.Productos;
import org.springframework.data.jpa.repository.JpaRepository;   
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Productos, Long> {
    
}
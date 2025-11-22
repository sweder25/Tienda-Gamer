package com.apiVentas.ventas.repository;

import com.apiVentas.ventas.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByUsuarioRut(Long usuarioRut);
    List<Venta> findByProductoId(Long productoId);
}
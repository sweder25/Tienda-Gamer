package com.apiVentas.ventas.repository;
import com.apiVentas.ventas.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
}
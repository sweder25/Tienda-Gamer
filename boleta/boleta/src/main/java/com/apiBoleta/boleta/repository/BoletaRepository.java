package com.apiBoleta.boleta.repository;

import com.apiBoleta.boleta.model.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {
    Optional<Boleta> findByVentaId(Long ventaId);
}
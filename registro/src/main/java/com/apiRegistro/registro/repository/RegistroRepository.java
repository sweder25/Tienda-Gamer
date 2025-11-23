package com.apiRegistro.registro.repository;

import com.apiRegistro.registro.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {
    Optional<Registro> findByEmail(String email);
}
package com.apiRegistro.registro.repository;

import com.apiRegistro.registro.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {
    Registro findByUsuarioId(Long usuarioId);
}
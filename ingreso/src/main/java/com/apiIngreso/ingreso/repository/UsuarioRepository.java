package com.apiIngreso.ingreso.repository;

import com.apiIngreso.ingreso.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar por email
    Optional<Usuario> findByEmail(String email);
    
    // Verificar si existe por email
    boolean existsByEmail(String email);
    
}
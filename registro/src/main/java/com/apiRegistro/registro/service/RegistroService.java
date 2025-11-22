package com.apiRegistro.registro.service;

import com.apiRegistro.registro.model.Registro;
import com.apiRegistro.registro.repository.RegistroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistroService {

    private final RegistroRepository repository;

    public RegistroService(RegistroRepository repository) {
        this.repository = repository;
    }

    public Registro crearRegistro(Registro registro) {
        // Validar que no exista un registro previo para este usuario
        Registro registroExistente = repository.findByUsuarioId(registro.getUsuarioId());
        if (registroExistente != null) {
            throw new RuntimeException("Ya existe un registro para el usuario con ID: " + registro.getUsuarioId());
        }
        return repository.save(registro);
    }

    public List<Registro> listarRegistros() {
        return repository.findAll();
    }

    public Registro obtenerRegistro(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void eliminarRegistro(Long id) {
        repository.deleteById(id);
    }

    public Registro obtenerPorUsuarioId(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }
}
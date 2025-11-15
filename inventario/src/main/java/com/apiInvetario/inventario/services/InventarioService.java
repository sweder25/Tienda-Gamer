
package com.apiInvetario.inventario.services;

import com.apiInvetario.inventario.model.Inventario;
import com.apiInvetario.inventario.repository.InventarioRepository;
import org.springframework.stereotype.Service;

@Service
public class InventarioService {

    private final InventarioRepository repo;

    public InventarioService(InventarioRepository repo) {
        this.repo = repo;
    }

    public Inventario agregarInventario(Long productoId, int cantidad) {
        Inventario inventario = repo.findByProductoId(productoId)
                .orElse(new Inventario(null, productoId, 0));
        inventario.setCantidadDisponible(inventario.getCantidadDisponible() + cantidad);
        return repo.save(inventario);
    }

    public Inventario actualizarInventario(Long productoId, int cantidad) {
        Inventario inventario = repo.findByProductoId(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en inventario"));
        inventario.setCantidadDisponible(cantidad);
        return repo.save(inventario);
    }

    public Integer consultarStock(Long productoId) {
        return repo.findByProductoId(productoId)
                .map(Inventario::getCantidadDisponible)
                .orElse(0);
    }
}

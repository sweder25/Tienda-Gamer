package com.apiInvetario.inventario.controller;

import com.apiInvetario.inventario.model.Inventario;
import com.apiInvetario.inventario.services.InventarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventario")
public class InventarioController {

    private final InventarioService service;

    public InventarioController(InventarioService service) {
        this.service = service;
    }

    @PostMapping("/agregar")
    public Inventario agregarStock(@RequestParam Long productoId, @RequestParam int cantidad) {
        return service.agregarInventario(productoId, cantidad);
    }

    @PutMapping("/actualizar")
    public Inventario actualizarStock(@RequestParam Long productoId, @RequestParam int cantidad) {
        return service.actualizarInventario(productoId, cantidad);
    }

    @GetMapping("/stock/{productoId}")
    public Integer consultarStock(@PathVariable Long productoId) {
        return service.consultarStock(productoId);
    }
}
package com.apiInvetario.inventario.controller;

import com.apiInvetario.inventario.model.Inventario;
import com.apiInvetario.inventario.services.InventarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "Operaciones para gestionar el inventario del sistema") 
public class InventarioController {

    private final InventarioService service;

    public InventarioController(InventarioService service) {
        this.service = service;
    }

    @Operation(summary = "Agregar stock al inventario", description = "Este endpoint permite agregar una cantidad específica de stock para un producto en el inventario.")
    @PostMapping("/agregar")
    public Inventario agregarStock(@RequestParam Long productoId, @RequestParam int cantidad) {
        return service.agregarInventario(productoId, cantidad);
    }

    @Operation(summary = "Actualizar stock del inventario", description = "Este endpoint permite actualizar la cantidad de stock de un producto en el inventario.")
    @PutMapping("/actualizar")
    public Inventario actualizarStock(@RequestParam Long productoId, @RequestParam int cantidad) {
        return service.actualizarInventario(productoId, cantidad);
    }

    @Operation(summary = "Consultar stock del inventario", description = "Este endpoint permite consultar la cantidad de stock disponible para un producto en el inventario.")
    @GetMapping("/stock/{productoId}")
    public Integer consultarStock(@PathVariable Long productoId) {
        return service.consultarStock(productoId);
    }

    @Operation(summary = "Eliminar inventario de un producto", description = "Este endpoint permite eliminar el inventario asociado a un producto específico.")
    @DeleteMapping("/eliminar/{productoId}")
    public void eliminarInventario(@PathVariable Long productoId) {
        service.eliminarInventario(productoId);
    }
}
package com.apiVentas.ventas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.apiVentas.ventas.model.Venta;
import com.apiVentas.ventas.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.annotations.OpenAPI30;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/ventas")
@Tag(name = "Ventas", description = "Operaciones para gestionar ventas del sistema")    
public class VentasController {

    @Autowired
    private VentaService ventaService;


    @Operation(summary = "Realizar una venta")
    @Description("Este endpoint permite registrar una nueva venta en el sistema.")  
    @PostMapping

    public void realizarVenta(@RequestBody Venta venta) {
        ventaService.realizarVenta(venta);
    }

    @Operation(summary = "Eliminar una venta")
    @Description("Este endpoint permite eliminar una venta existente en el sistema.")   
    @PutMapping("/{id}")
     public ResponseEntity<Void> eliminarVenta(@PathVariable Long id) {
        ventaService.eliminarVenta(id);
        return ResponseEntity.noContent().build();

    }

    @Operation(summary = "Obtener una venta por ID")
    @Description("Este endpoint permite obtener los detalles de una venta específica utilizando su ID.")
    @GetMapping("/ventas/{id}")
    public ResponseEntity<Venta> obtenerVenta(@PathVariable Long id) {
        Venta venta = ventaService.obtenerVenta(id);
        if (venta != null) {
            return ResponseEntity.ok(venta);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

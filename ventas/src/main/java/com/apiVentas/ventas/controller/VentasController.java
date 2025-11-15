package com.apiVentas.ventas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.apiVentas.ventas.model.Venta;
import com.apiVentas.ventas.service.VentaService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/ventas")


public class VentasController {

    @Autowired
    private VentaService ventaService;

    @PostMapping
    public void realizarVenta(@RequestBody Venta venta) {
        ventaService.realizarVenta(venta);
    }

    @GetMapping("/{id}")
     public ResponseEntity<Void> eliminarVenta(@PathVariable Long id) {
        ventaService.eliminarVenta(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/ventas/{id}")
    public ResponseEntity<Venta> obtenerVenta(@PathVariable Long id) {
        Venta venta = ventaService.obtenerVenta(id);
        if (venta != null) {
            return ResponseEntity.ok(venta);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ventas/nueva")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    

}

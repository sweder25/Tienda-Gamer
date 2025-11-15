package com.apiVentas.ventas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apiVentas.ventas.repository.VentaRepository;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    public VentaRepository getVentaRepository() {
        return ventaRepository;
    }

    public void setVentaRepository(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    public void realizarVenta(com.apiVentas.ventas.model.Venta venta) {
        ventaRepository.save(venta);
    }
    
    public void eliminarVenta(Long id) {
        ventaRepository.deleteById(id);
    }

    public com.apiVentas.ventas.model.Venta obtenerVenta(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

}

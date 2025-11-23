package com.apiTienda.tienda.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "services")



//Clase para mapear las URLs de los servicios desde application.properties
//@Data genera getters y setters automáticamente
@Data
public class ServicesProperties {
    private ServiceUrl ingreso = new ServiceUrl();
    private ServiceUrl productos = new ServiceUrl();
    private ServiceUrl registro = new ServiceUrl();
    private ServiceUrl usuarios = new ServiceUrl();
    private ServiceUrl ventas = new ServiceUrl();
    private ServiceUrl boleta = new ServiceUrl();
    
    @Data
    public static class ServiceUrl {
        private String url;
    }
}
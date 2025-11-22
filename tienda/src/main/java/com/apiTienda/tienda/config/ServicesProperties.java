package com.apiTienda.tienda.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "services")
public class ServicesProperties {
    private String productosUrl;
    private String registrosUrl;
    private String usuariosUrl;
    private String ventasUrl;
     private String boletaUrl;

    public String getProductosUrl() { return productosUrl; }
    public void setProductosUrl(String productosUrl) { this.productosUrl = productosUrl; }

    public String getRegistrosUrl() { return registrosUrl; }
    public void setRegistrosUrl(String registrosUrl) { this.registrosUrl = registrosUrl; }

    public String getUsuariosUrl() { return usuariosUrl; }
    public void setUsuariosUrl(String usuariosUrl) { this.usuariosUrl = usuariosUrl; }

    public String getVentasUrl() { return ventasUrl; }
    public void setVentasUrl(String ventasUrl) { this.ventasUrl = ventasUrl; }

    public String getBoletaUrl() { return boletaUrl; }
    public void setBoletaUrl(String boletaUrl) { this.boletaUrl = boletaUrl; }



}
package com.apiTienda.tienda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class TiendaApplicationTests {

    public static void main(String[] args) {
        SpringApplication.run(TiendaApplication.class, args);
    }

}
package edu.tecnm;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        
        // 1. Mapeo para IMÁGENES DE PRODUCTO (Ruta que ya tenías)
        registry
            .addResourceHandler("/producto-img/**")
            .addResourceLocations(
                "file:/C:/producto/"); // <--- RUTA FÍSICA DE PRODUCTOS
        
        // 2. Mapeo para IMÁGENES DE CLIENTE (¡NUEVA RUTA VITAL para clientes!)
        // Esta URL /imagenes/img/Cliente/ es la que usa listaClientes.html
        registry
            .addResourceHandler("/imagenes/img/Cliente/**")
            .addResourceLocations(
                "file:/C:/ruta-externa/clientes/"); // <--- RUTA FÍSICA DE CLIENTES (¡AJUSTA AQUÍ!)
    }
}
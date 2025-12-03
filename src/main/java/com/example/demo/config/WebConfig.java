package com.example.demo.config; // 🛑 VERIFICA TU PAQUETE REAL AQUÍ (Tu captura dice 'com.example.demo.config')

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        
        // 1. Mapeo para recursos ESTÁTICOS INTERNOS (CSS, JS, imágenes dentro de /static)
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

        // 🛑 2. MAPEO CLAVE: Para la carpeta de imágenes EXTERNA (C:/producto/)
        // Esto permite que la URL /imagenes/** apunte al directorio C:/producto/
        // ESTO SOLUCIONARÁ EL ERROR 404
        registry.addResourceHandler("/imagenes/**")
                .addResourceLocations("file:///C:/producto/"); 

        // 3. Mapeo específico para imágenes de Cliente (si es necesario)
        registry.addResourceHandler("/img/Cliente/**")
                .addResourceLocations("file:src/main/resources/static/img/Cliente/");
    }
}
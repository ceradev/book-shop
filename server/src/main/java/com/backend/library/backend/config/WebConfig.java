package com.backend.library.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    /**
     * Configura la ruta para mapear las solicitudes a la carpeta C:/images/uploads/books/
     */
    @Override
    public void addResourceHandlers(@SuppressWarnings("null") ResourceHandlerRegistry registry) {
        // Mapear todas las solicitudes a /images/** a la carpeta C:/images/uploads/books/
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///C:/images/uploads/books/");
    }
    



}

package com.org.ezequielBolzi.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("API REST Café Rewards Management System"
                                )
                        .version("1.0.0")
                        .description("Esta API REST es diseñada para administrar el sistema de recompensas de café de una Empresa. " +
                                "Permite a los usuarios registrar películas y series, así como asignar un like a los post de otros empleados." +
                                " Además, facilita la gestión de empleados, incluyendo detalles como nombre, correo electrónico, rol (frontend o backend), edad, y permite identificar " +
                                "al empleado que ha propuesto la película o serie más valorada. La API proporciona funcionalidades para visualizar, agregar, actualizar " +
                                "y eliminar registros de películas, series y empleados. También ofrece filtros  para buscar entre las propuestas basándose en género, puntuación y año."));
    }
}


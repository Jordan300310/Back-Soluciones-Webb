package com.example.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI erpOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("ERP API - Simple")
            .version("v1")
            .description("API para auth, productos, pedidos y compras (MVP)")
            .contact(new Contact()
                .name("Equipo FYJ")
                .email("soporte@fyj.pe")))
        .servers(List.of(
            new Server().url("http://localhost:8080").description("Local")
        ));
  }
}

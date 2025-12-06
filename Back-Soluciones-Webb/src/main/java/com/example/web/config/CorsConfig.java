
package com.example.web.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(
            "http://localhost:4200",
            "http://127.0.0.1:4200",
            "http://localhost:5173",
            "http://127.0.0.1:5173"
        )
        .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true);
  }
}

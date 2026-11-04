
package pe.edu.utp.thanosgym.gateway.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
        CorsConfiguration config = new CorsConfiguration();

        // Direcciones desde las que se permite acceder
        // durante el desarrollo local de la página web.
        config.setAllowedOrigins(List.of(
                "http://127.0.0.1:5500",
                "http://localhost:5500"
        ));

        // Métodos HTTP permitidos.
        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));

        // Encabezados que puede enviar el navegador.
        config.setAllowedHeaders(List.of("*"));

        // Permite leer este encabezado si el servidor lo devuelve.
        config.setExposedHeaders(List.of("Authorization"));

        // No usamos cookies de autenticación en este flujo.
        config.setAllowCredentials(false);

        // Tiempo durante el cual el navegador puede reutilizar
        // la respuesta de preflight.
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        CorsFilter filter = new CorsFilter(source);

        FilterRegistrationBean<CorsFilter> registration =
                new FilterRegistrationBean<>(filter);

        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return registration;
    }
}

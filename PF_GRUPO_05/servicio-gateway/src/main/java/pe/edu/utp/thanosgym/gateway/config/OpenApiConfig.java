package pe.edu.utp.thanosgym.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI api() {
        return new OpenAPI().info(new Info()
                .title("ThanosGym - API Gateway")
                .version("1.0.0")
                .description("Punto de entrada para consumir los servicios REST de ThanosGym."));
    }
}

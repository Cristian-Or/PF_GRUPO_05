package pe.edu.utp.thanosgym.auth.config;
import io.swagger.v3.oas.models.*;import io.swagger.v3.oas.models.info.Info;import io.swagger.v3.oas.models.security.SecurityScheme;import org.springframework.context.annotation.*;
@Configuration public class OpenApiConfig{@Bean OpenAPI api(){return new OpenAPI().info(new Info().title("ThanosGym | Acceso").version("1.0").description("Inicio de sesión y manejo de tokens de acceso.")).components(new Components().addSecuritySchemes("bearerAuth",new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));}}

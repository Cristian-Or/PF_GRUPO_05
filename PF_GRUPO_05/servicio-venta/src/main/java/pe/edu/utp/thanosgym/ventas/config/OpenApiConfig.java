package pe.edu.utp.thanosgym.ventas.config;
import io.swagger.v3.oas.models.*;import io.swagger.v3.oas.models.info.Info;import io.swagger.v3.oas.models.security.SecurityScheme;import org.springframework.context.annotation.*;
@Configuration public class OpenApiConfig {@Bean OpenAPI api(){return new OpenAPI().info(new Info().title("ThanosGym | Ventas").version("1.0").description("Registro de las ventas de membresías.")).components(new Components().addSecuritySchemes("bearerAuth",new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));}}

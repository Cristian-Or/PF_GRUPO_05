package pe.edu.utp.thanosgym.auth.controller;
import io.swagger.v3.oas.annotations.Operation;import io.swagger.v3.oas.annotations.security.SecurityRequirement;import jakarta.validation.Valid;import pe.edu.utp.thanosgym.auth.dto.*;import pe.edu.utp.thanosgym.auth.service.AutenticacionService;import org.springframework.http.HttpHeaders;import org.springframework.web.bind.annotation.*;import java.util.Map;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController @RequestMapping("/api/v1/auth") @Tag(name="Acceso") public class AutenticacionController{
 private final AutenticacionService service;public AutenticacionController(AutenticacionService s){service=s;}
 @PostMapping("/login") @Operation(summary="Iniciar sesión") LoginResponse login(@Valid @RequestBody CredencialesRequest r){return service.login(r);}
 @GetMapping("/validar") @SecurityRequirement(name="bearerAuth") @Operation(summary="Validar token") TokenValidoResponse validar(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){return service.validar(token);}
 @PostMapping("/renovar") @SecurityRequirement(name="bearerAuth") @Operation(summary="Renovar token") RenovacionResponse renovar(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){return service.renovar(token);}
 @PostMapping("/logout") @SecurityRequirement(name="bearerAuth") @Operation(summary="Cerrar sesión") Map<String,String> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){service.logout(token);return Map.of("mensaje","Sesión cerrada correctamente");}
}

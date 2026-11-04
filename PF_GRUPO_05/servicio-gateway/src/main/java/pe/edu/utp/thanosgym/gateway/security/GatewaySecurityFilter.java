package pe.edu.utp.thanosgym.gateway.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.edu.utp.thanosgym.gateway.config.GatewayProperties;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Component
public class GatewaySecurityFilter extends OncePerRequestFilter {
    private final GatewayProperties properties;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public GatewaySecurityFilter(GatewayProperties properties, RestClient restClient, ObjectMapper objectMapper) {
        this.properties = properties;
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !properties.getSecurity().isEnabled()
                || path.equals("/")
                || path.startsWith("/actuator")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/api/v1/auth");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            writeError(response, HttpStatus.UNAUTHORIZED, "TOKEN_REQUERIDO", "Debes enviar Authorization: Bearer <token>.");
            return;
        }

        String authBaseUrl = properties.getRoutes().get("auth");
        try {
            TokenValidoResponse token = restClient.get()
                    .uri(authBaseUrl + "/api/v1/auth/validar")
                    .header(HttpHeaders.AUTHORIZATION, authorization)
                    .retrieve()
                    .body(TokenValidoResponse.class);

            if (token == null || !token.valido()) {
                writeError(response, HttpStatus.UNAUTHORIZED, "TOKEN_INVALIDO", "El token no es valido o ya expiro.");
                return;
            }
        } catch (RestClientException ex) {
            writeError(response, HttpStatus.SERVICE_UNAVAILABLE, "AUTH_NO_DISPONIBLE", "No se pudo validar el token en este momento.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeError(HttpServletResponse response, HttpStatus status, String code, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "error", code,
                "message", message
        ));
    }

    private record TokenValidoResponse(boolean valido) {
    }
}

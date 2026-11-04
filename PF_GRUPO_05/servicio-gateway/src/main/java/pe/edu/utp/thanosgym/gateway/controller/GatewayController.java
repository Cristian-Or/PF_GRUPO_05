package pe.edu.utp.thanosgym.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import pe.edu.utp.thanosgym.gateway.config.GatewayProperties;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Gateway")
public class GatewayController {
    private static final List<String> HOP_BY_HOP_HEADERS = List.of(
            "connection", "keep-alive", "proxy-authenticate", "proxy-authorization",
            "te", "trailer", "transfer-encoding", "upgrade", "host", "content-length"
    );

    private final GatewayProperties properties;
    private final RestClient restClient;

    public GatewayController(GatewayProperties properties, RestClient restClient) {
        this.properties = properties;
        this.restClient = restClient;
    }

    @RequestMapping("/")
    @Operation(summary = "Ver rutas disponibles del gateway")
    Map<String, Object> inicio() {
        return Map.of(
                "servicio", "ThanosGym API Gateway",
                "puerto", 8080,
                "uso", "Primero inicia sesion en /api/v1/auth/login y luego consume las demas rutas con Bearer token.",
                "rutas", Map.of(
                        "auth", "/api/v1/auth/**",
                        "clientes", "/api/v1/clientes/**",
                        "membresias", "/api/v1/membresias/**",
                        "ventas", "/api/v1/ventas/**",
                        "pagos", "/api/v1/pagos/**",
                        "notificaciones", "/api/v1/notificaciones/**",
                        "orquestacion", "/api/v1/orquestacion/**"
                )
        );
    }

    @RequestMapping("/api/v1/**")
    @Operation(summary = "Redirigir la peticion al microservicio correspondiente")
    ResponseEntity<byte[]> redirigir(HttpServletRequest request, @RequestBody(required = false) byte[] body) {
        String key = routeKey(request.getRequestURI());
        String baseUrl = properties.getRoutes().get(key);
        if (baseUrl == null) {
            return json(HttpStatus.NOT_FOUND, "RUTA_NO_CONFIGURADA", "El gateway no tiene una ruta para " + request.getRequestURI());
        }

        URI target = URI.create(baseUrl + request.getRequestURI() + query(request));
        HttpMethod method = HttpMethod.valueOf(request.getMethod());

        try {
            RestClient.RequestBodySpec requestSpec = restClient.method(method)
                    .uri(target)
                    .headers(headers -> copyRequestHeaders(request, headers));

            if (body != null && body.length > 0) {
                requestSpec.body(body);
            }

            return requestSpec.exchange((clientRequest, clientResponse) -> {
                byte[] responseBody = StreamUtils.copyToByteArray(clientResponse.getBody());
                HttpHeaders responseHeaders = new HttpHeaders();
                clientResponse.getHeaders().forEach((name, values) -> {
                    if (!HOP_BY_HOP_HEADERS.contains(name.toLowerCase())) {
                        responseHeaders.put(name, values);
                    }
                });
                return ResponseEntity.status(clientResponse.getStatusCode())
                        .headers(responseHeaders)
                        .body(responseBody);
            });
        } catch (RestClientException ex) {
            return json(HttpStatus.BAD_GATEWAY, "SERVICIO_NO_DISPONIBLE", "No se pudo contactar el servicio destino para " + request.getRequestURI());
        }
    }

    private String routeKey(String uri) {
        String[] parts = uri.split("/");
        if (parts.length < 4) {
            return "";
        }
        return switch (parts[3]) {
            case "auth" -> "auth";
            case "clientes" -> "clientes";
            case "membresias" -> "membresias";
            case "ventas" -> "ventas";
            case "pagos" -> "pagos";
            case "notificaciones" -> "notificaciones";
            case "orquestacion" -> "orquestacion";
            default -> "";
        };
    }

    private String query(HttpServletRequest request) {
        return request.getQueryString() == null ? "" : "?" + request.getQueryString();
    }

    private void copyRequestHeaders(HttpServletRequest request, HttpHeaders headers) {
        request.getHeaderNames().asIterator().forEachRemaining(name -> {
            if (!HOP_BY_HOP_HEADERS.contains(name.toLowerCase())) {
                headers.put(name, java.util.Collections.list(request.getHeaders(name)));
            }
        });
    }

    private ResponseEntity<byte[]> json(HttpStatus status, String error, String message) {
        String body = """
                {"timestamp":"%s","status":%d,"error":"%s","message":"%s"}
                """.formatted(Instant.now(), status.value(), error, message);
        return ResponseEntity.status(status)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(body.getBytes(StandardCharsets.UTF_8));
    }
}

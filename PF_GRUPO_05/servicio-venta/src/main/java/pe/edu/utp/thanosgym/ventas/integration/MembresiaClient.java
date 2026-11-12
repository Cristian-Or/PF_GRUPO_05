package pe.edu.utp.thanosgym.ventas.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import pe.edu.utp.thanosgym.ventas.exception.ApiException;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.time.Duration;

@Component
public class MembresiaClient {
    private final RestClient client;
    private final boolean validar;

    public MembresiaClient(@Value("${thanosgym.membresias.url:http://localhost:9002}") String url,
                           @Value("${thanosgym.integraciones.validar:true}") boolean validar,
                           @Value("${thanosgym.integraciones.connect-timeout-ms:2000}") long connectTimeout,
                           @Value("${thanosgym.integraciones.read-timeout-ms:3000}") long readTimeout) {
        this.client = RestClient.builder()
                .baseUrl(url)
                .requestFactory(requestFactory(connectTimeout, readTimeout))
                .build();
        this.validar = validar;
    }

    public Plan obtenerPlan(String id) {
        if (!validar) return new Plan(id, id, 30, BigDecimal.ZERO, "Plan de prueba", true);
        try {
            Plan plan = client.get()
                    .uri("/api/v1/membresias/planes/{id}", id)
                    .retrieve()
                    .body(Plan.class);
            if (plan == null) throw noDisponible();
            return plan;
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "PLAN_NO_DISPONIBLE",
                        "El plan no existe o está inactivo");
            }
            throw new ApiException(HttpStatus.BAD_GATEWAY, "SERVICIO_MEMBRESIA_NO_DISPONIBLE",
                    "El servicio de membresías respondió con error " + ex.getStatusCode().value());
        } catch (ResourceAccessException ex) {
            throw noDisponible();
        }
    }

    private ApiException noDisponible() {
        return new ApiException(HttpStatus.BAD_GATEWAY, "SERVICIO_MEMBRESIA_NO_DISPONIBLE",
                "El servicio de membresías no está disponible");
    }

    private static JdkClientHttpRequestFactory requestFactory(long connectTimeout, long readTimeout) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(connectTimeout))
                .build();
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(Duration.ofMillis(readTimeout));
        return factory;
    }

    public record Plan(String idPlan, String nombre, Integer duracionDias, BigDecimal precio,
                       String descripcion, boolean activo) {}
}

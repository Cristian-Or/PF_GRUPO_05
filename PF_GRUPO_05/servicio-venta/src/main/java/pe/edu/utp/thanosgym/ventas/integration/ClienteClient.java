package pe.edu.utp.thanosgym.ventas.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import pe.edu.utp.thanosgym.ventas.exception.ApiException;

import java.net.http.HttpClient;
import java.time.Duration;

@Component
public class ClienteClient {
    private final RestClient client;
    private final boolean validar;

    public ClienteClient(@Value("${thanosgym.clientes.url:http://localhost:9001}") String url,
                         @Value("${thanosgym.integraciones.validar:true}") boolean validar,
                         @Value("${thanosgym.integraciones.connect-timeout-ms:2000}") long connectTimeout,
                         @Value("${thanosgym.integraciones.read-timeout-ms:3000}") long readTimeout) {
        this.client = RestClient.builder()
                .baseUrl(url)
                .requestFactory(requestFactory(connectTimeout, readTimeout))
                .build();
        this.validar = validar;
    }

    public void verificar(Long idCliente) {
        if (!validar) return;
        try {
            client.get()
                    .uri("/api/v1/clientes/{id}", idCliente)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "CLIENTE_NO_ENCONTRADO", "El cliente no existe");
            }
            throw new ApiException(HttpStatus.BAD_GATEWAY, "SERVICIO_CLIENTE_NO_DISPONIBLE",
                    "El servicio de clientes respondió con error " + ex.getStatusCode().value());
        } catch (ResourceAccessException ex) {
            throw noDisponible();
        }
    }

    private ApiException noDisponible() {
        return new ApiException(HttpStatus.BAD_GATEWAY, "SERVICIO_CLIENTE_NO_DISPONIBLE",
                "El servicio de clientes no está disponible");
    }

    private static JdkClientHttpRequestFactory requestFactory(long connectTimeout, long readTimeout) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(connectTimeout))
                .build();
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(Duration.ofMillis(readTimeout));
        return factory;
    }
}

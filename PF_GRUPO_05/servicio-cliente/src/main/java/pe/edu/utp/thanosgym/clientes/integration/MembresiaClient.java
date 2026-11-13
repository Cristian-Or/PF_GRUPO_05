package pe.edu.utp.thanosgym.clientes.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Optional;

@Component
public class MembresiaClient {
    private final RestClient client;

    public MembresiaClient(@Value("${thanosgym.membresias.url:http://localhost:9002}") String url,
                           @Value("${thanosgym.membresias.connect-timeout-ms:1000}") long connectTimeout,
                           @Value("${thanosgym.membresias.read-timeout-ms:1500}") long readTimeout) {
        this.client = RestClient.builder()
                .baseUrl(url)
                .requestFactory(requestFactory(connectTimeout, readTimeout))
                .build();
    }

    public Optional<MembresiaResumen> buscarPorCliente(Long idCliente) {
        try {
            return Optional.ofNullable(client.get()
                    .uri("/api/v1/membresias/cliente/{idCliente}", idCliente)
                    .retrieve()
                    .body(MembresiaResumen.class));
        } catch (RestClientException ex) {
            return Optional.empty();
        }
    }

    private static JdkClientHttpRequestFactory requestFactory(long connectTimeout, long readTimeout) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(connectTimeout))
                .build();
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(Duration.ofMillis(readTimeout));
        return factory;
    }

    public record MembresiaResumen(Long idMembresia, String estado) {
    }
}

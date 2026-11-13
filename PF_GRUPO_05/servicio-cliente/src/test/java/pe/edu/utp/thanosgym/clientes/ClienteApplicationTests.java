package pe.edu.utp.thanosgym.clientes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:clientes;MODE=MySQL")
class ClienteApplicationTests {
    @Test void contextLoads() {}
}

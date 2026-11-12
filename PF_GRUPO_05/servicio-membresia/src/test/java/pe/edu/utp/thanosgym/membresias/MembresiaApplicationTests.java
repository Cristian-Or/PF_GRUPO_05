package pe.edu.utp.thanosgym.membresias;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties={"spring.datasource.url=jdbc:h2:mem:membresias;MODE=MySQL","thanosgym.clientes.validar=false"})
class MembresiaApplicationTests { @Test void contextLoads() {} }

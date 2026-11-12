package pe.edu.utp.thanosgym.ventas;
import org.junit.jupiter.api.Test;import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest(properties={"spring.datasource.url=jdbc:h2:mem:ventas;MODE=MySQL","thanosgym.integraciones.validar=false"}) class VentaApplicationTests {@Test void contextLoads(){}}

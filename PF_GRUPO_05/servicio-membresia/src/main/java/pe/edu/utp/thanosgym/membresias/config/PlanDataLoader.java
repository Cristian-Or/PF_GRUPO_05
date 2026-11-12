package pe.edu.utp.thanosgym.membresias.config;

import pe.edu.utp.thanosgym.membresias.model.Plan;
import pe.edu.utp.thanosgym.membresias.repository.PlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import java.math.BigDecimal;

@Configuration
public class PlanDataLoader {
    @Bean CommandLineRunner cargarPlanes(PlanRepository repository) { return args -> {
        if (repository.count()==0) repository.saveAll(java.util.List.of(
            new Plan("MENSUAL","Plan Mensual",30,new BigDecimal("60.00"),"Acceso al gimnasio durante 30 días"),
            new Plan("TRIMESTRAL","Plan Trimestral",90,new BigDecimal("160.00"),"Acceso por 90 días con ahorro"),
            new Plan("ANUAL","Plan Anual",365,new BigDecimal("550.00"),"Acceso por un año completo")
        ));
    }; }
}

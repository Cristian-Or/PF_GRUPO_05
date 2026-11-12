package pe.edu.utp.thanosgym.membresias.repository;

import pe.edu.utp.thanosgym.membresias.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.*;
public interface MembresiaRepository extends JpaRepository<Membresia, Long> {
    Optional<Membresia> findFirstByIdClienteOrderByFechaFinDesc(Long idCliente);
    List<Membresia> findByEstadoAndFechaFinLessThanEqual(EstadoMembresia estado, LocalDate fechaLimite);
}

package pe.edu.utp.thanosgym.clientes.repository;

import pe.edu.utp.thanosgym.clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByDni(String dni);
    boolean existsByDni(String dni);
    List<Cliente> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombres, String apellidos);
}

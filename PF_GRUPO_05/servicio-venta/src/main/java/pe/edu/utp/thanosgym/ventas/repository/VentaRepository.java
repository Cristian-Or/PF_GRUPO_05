package pe.edu.utp.thanosgym.ventas.repository;

import pe.edu.utp.thanosgym.ventas.model.Venta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta,Long> {
    List<Venta> findByIdClienteOrderByFechaRegistroDesc(Long idCliente, Pageable pageable);
}

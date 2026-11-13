package pe.edu.utp.thanosgym.pagos.repository;
import pe.edu.utp.thanosgym.pagos.model.Pago;import org.springframework.data.domain.Pageable;import org.springframework.data.jpa.repository.JpaRepository;import java.util.List;
public interface PagoRepository extends JpaRepository<Pago,Long>{boolean existsByIdVenta(Long idVenta);List<Pago> findByIdClienteOrderByTimestampDesc(Long idCliente,Pageable pageable);}

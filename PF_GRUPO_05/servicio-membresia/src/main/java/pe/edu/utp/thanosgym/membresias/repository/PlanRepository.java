package pe.edu.utp.thanosgym.membresias.repository;

import pe.edu.utp.thanosgym.membresias.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PlanRepository extends JpaRepository<Plan, String> { List<Plan> findByActivoTrue(); }

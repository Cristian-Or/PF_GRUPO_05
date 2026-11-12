package pe.edu.utp.thanosgym.membresias.dto;

import pe.edu.utp.thanosgym.membresias.model.EstadoMembresia;
import java.time.LocalDate;
public record MembresiaResponse(Long idMembresia, Long idCliente, PlanResponse plan,
        LocalDate fechaInicio, LocalDate fechaFin, long diasRestantes, EstadoMembresia estado) {}

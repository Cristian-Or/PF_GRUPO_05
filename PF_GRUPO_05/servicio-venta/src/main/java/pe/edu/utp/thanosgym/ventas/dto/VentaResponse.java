package pe.edu.utp.thanosgym.ventas.dto;

import pe.edu.utp.thanosgym.ventas.model.EstadoVenta;
import java.math.BigDecimal;
import java.time.*;

public record VentaResponse(Long idVenta, Long idCliente, PlanResumen plan, LocalDate fechaInicio,
        LocalDate fechaFin, EstadoVenta estado, BigDecimal montoCobrado, String observaciones,
        LocalDateTime fechaRegistro) {
    public record PlanResumen(String idPlan, String nombre, Integer duracionDias) {}
}

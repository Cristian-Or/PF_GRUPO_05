package pe.edu.utp.thanosgym.membresias.dto;

import java.math.BigDecimal;
public record PlanResponse(String idPlan, String nombre, Integer duracionDias, BigDecimal precio,
                           String descripcion, boolean activo) {}

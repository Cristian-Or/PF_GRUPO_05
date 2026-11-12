package pe.edu.utp.thanosgym.membresias.dto;

import jakarta.validation.constraints.NotNull;
import pe.edu.utp.thanosgym.membresias.model.EstadoMembresia;
public record EstadoRequest(@NotNull EstadoMembresia estado) {}

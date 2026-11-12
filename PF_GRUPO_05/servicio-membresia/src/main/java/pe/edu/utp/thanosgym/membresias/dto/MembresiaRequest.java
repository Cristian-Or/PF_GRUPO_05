package pe.edu.utp.thanosgym.membresias.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record MembresiaRequest(@NotNull @Positive Long idCliente, @NotBlank String idPlan,
                               @NotNull LocalDate fechaInicio) {}

package pe.edu.utp.thanosgym.ventas.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record VentaRequest(@NotNull @Positive Long idCliente, @NotBlank String idPlan,
        @NotNull LocalDate fechaInicio, @NotNull @DecimalMin(value="0.01") BigDecimal montoCobrado,
        @Size(max=300) String observaciones) {}

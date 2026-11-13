package pe.edu.utp.thanosgym.clientes.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ClienteRequest(
        @NotBlank @Pattern(regexp = "\\d{8}", message = "debe contener exactamente 8 dígitos") String dni,
        @NotBlank @Size(max = 80) String nombres,
        @NotBlank @Size(max = 80) String apellidos,
        @NotBlank @Pattern(regexp = "9\\d{8}", message = "debe ser un celular peruano de 9 dígitos") String telefono,
        @Email @Size(max = 120) String email,
        @Past LocalDate fechaNacimiento,
        @Size(max = 30) String plan) {}

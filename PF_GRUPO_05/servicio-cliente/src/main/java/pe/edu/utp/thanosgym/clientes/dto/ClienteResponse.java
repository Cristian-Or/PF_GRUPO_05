package pe.edu.utp.thanosgym.clientes.dto;

import pe.edu.utp.thanosgym.clientes.model.EstadoMembresia;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClienteResponse(Long idCliente, String dni, String nombres, String apellidos,
        String telefono, String email, LocalDate fechaNacimiento, String plan,
        LocalDateTime fechaRegistro, EstadoMembresia estadoMembresia, Long idMembresia) {}

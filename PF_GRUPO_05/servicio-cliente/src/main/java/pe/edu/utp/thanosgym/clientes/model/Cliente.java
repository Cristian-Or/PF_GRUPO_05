package pe.edu.utp.thanosgym.clientes.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientes", uniqueConstraints = @UniqueConstraint(name = "uk_cliente_dni", columnNames = "dni"))
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;
    @Column(nullable = false, length = 8) private String dni;
    @Column(nullable = false, length = 80) private String nombres;
    @Column(nullable = false, length = 80) private String apellidos;
    @Column(nullable = false, length = 9) private String telefono;
    @Column(length = 120) private String email;
    private LocalDate fechaNacimiento;
    @Column(length = 30) private String plan;
    private Long idMembresia;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private EstadoMembresia estadoMembresia = EstadoMembresia.SIN_MEMBRESIA;
    @Column(nullable = false, updatable = false) private LocalDateTime fechaRegistro;

    @PrePersist void prePersist() { fechaRegistro = LocalDateTime.now(); }

    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getPlan() { return plan; }
    public void setPlan(String plan) { this.plan = plan; }
    public Long getIdMembresia() { return idMembresia; }
    public void setIdMembresia(Long idMembresia) { this.idMembresia = idMembresia; }
    public EstadoMembresia getEstadoMembresia() { return estadoMembresia; }
    public void setEstadoMembresia(EstadoMembresia estadoMembresia) { this.estadoMembresia = estadoMembresia; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}

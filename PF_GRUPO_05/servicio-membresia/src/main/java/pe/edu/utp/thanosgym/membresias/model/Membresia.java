package pe.edu.utp.thanosgym.membresias.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity @Table(name = "membresias", indexes = @Index(name = "idx_membresia_cliente", columnList = "idCliente"))
public class Membresia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long idMembresia;
    @Column(nullable = false) private Long idCliente;
    @ManyToOne(optional = false, fetch = FetchType.EAGER) @JoinColumn(name = "id_plan") private Plan plan;
    @Column(nullable = false) private LocalDate fechaInicio;
    @Column(nullable = false) private LocalDate fechaFin;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 15) private EstadoMembresia estado;

    public Long getIdMembresia() { return idMembresia; }
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
    public Plan getPlan() { return plan; }
    public void setPlan(Plan plan) { this.plan = plan; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public EstadoMembresia getEstado() { return estado; }
    public void setEstado(EstadoMembresia estado) { this.estado = estado; }
}

package pe.edu.utp.thanosgym.ventas.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;

@Entity @Table(name="ventas", indexes=@Index(name="idx_venta_cliente", columnList="idCliente"))
public class Venta {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long idVenta;
    @Column(nullable=false) private Long idCliente;
    @Column(nullable=false, length=20) private String idPlan;
    @Column(nullable=false, length=60) private String nombrePlan;
    @Column(nullable=false) private Integer duracionDias;
    @Column(nullable=false) private LocalDate fechaInicio;
    @Column(nullable=false) private LocalDate fechaFin;
    @Column(nullable=false, precision=10, scale=2) private BigDecimal montoCobrado;
    @Column(length=300) private String observaciones;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private EstadoVenta estado;
    @Column(nullable=false, updatable=false) private LocalDateTime fechaRegistro;

    @PrePersist void prePersist(){ fechaRegistro=LocalDateTime.now(); }
    public Long getIdVenta(){return idVenta;} public Long getIdCliente(){return idCliente;} public void setIdCliente(Long v){idCliente=v;}
    public String getIdPlan(){return idPlan;} public void setIdPlan(String v){idPlan=v;} public String getNombrePlan(){return nombrePlan;} public void setNombrePlan(String v){nombrePlan=v;}
    public Integer getDuracionDias(){return duracionDias;} public void setDuracionDias(Integer v){duracionDias=v;} public LocalDate getFechaInicio(){return fechaInicio;} public void setFechaInicio(LocalDate v){fechaInicio=v;}
    public LocalDate getFechaFin(){return fechaFin;} public void setFechaFin(LocalDate v){fechaFin=v;} public BigDecimal getMontoCobrado(){return montoCobrado;} public void setMontoCobrado(BigDecimal v){montoCobrado=v;}
    public String getObservaciones(){return observaciones;} public void setObservaciones(String v){observaciones=v;} public EstadoVenta getEstado(){return estado;} public void setEstado(EstadoVenta v){estado=v;}
    public LocalDateTime getFechaRegistro(){return fechaRegistro;}
}

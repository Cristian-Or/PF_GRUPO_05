package pe.edu.utp.thanosgym.membresias.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name = "planes")
public class Plan {
    @Id @Column(length = 20) private String idPlan;
    @Column(nullable = false, length = 60) private String nombre;
    @Column(nullable = false) private Integer duracionDias;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal precio;
    @Column(length = 300) private String descripcion;
    @Column(nullable = false) private boolean activo = true;

    public Plan() {}
    public Plan(String idPlan, String nombre, Integer duracionDias, BigDecimal precio, String descripcion) {
        this.idPlan=idPlan; this.nombre=nombre; this.duracionDias=duracionDias; this.precio=precio; this.descripcion=descripcion;
    }
    public String getIdPlan() { return idPlan; }
    public void setIdPlan(String idPlan) { this.idPlan = idPlan; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getDuracionDias() { return duracionDias; }
    public void setDuracionDias(Integer duracionDias) { this.duracionDias = duracionDias; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}

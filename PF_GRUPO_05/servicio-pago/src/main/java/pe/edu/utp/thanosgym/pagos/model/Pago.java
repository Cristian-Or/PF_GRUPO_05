package pe.edu.utp.thanosgym.pagos.model;

import jakarta.persistence.*;import java.math.BigDecimal;import java.time.LocalDateTime;
@Entity @Table(name="pagos",uniqueConstraints=@UniqueConstraint(name="uk_pago_venta",columnNames="idVenta"),indexes=@Index(name="idx_pago_cliente",columnList="idCliente"))
public class Pago {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long idPago; @Column(nullable=false) private Long idVenta; @Column(nullable=false) private Long idCliente;
 @Column(nullable=false,precision=10,scale=2) private BigDecimal monto; @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private MetodoPago metodoPago;
 @Column(length=300) private String observaciones; @Enumerated(EnumType.STRING) @Column(nullable=false,length=15) private EstadoPago estado; @Column(nullable=false,updatable=false) private LocalDateTime timestamp;
 @PrePersist void prePersist(){timestamp=LocalDateTime.now();}
 public Long getIdPago(){return idPago;} public Long getIdVenta(){return idVenta;} public void setIdVenta(Long v){idVenta=v;} public Long getIdCliente(){return idCliente;} public void setIdCliente(Long v){idCliente=v;}
 public BigDecimal getMonto(){return monto;} public void setMonto(BigDecimal v){monto=v;} public MetodoPago getMetodoPago(){return metodoPago;} public void setMetodoPago(MetodoPago v){metodoPago=v;}
 public String getObservaciones(){return observaciones;} public void setObservaciones(String v){observaciones=v;} public EstadoPago getEstado(){return estado;} public void setEstado(EstadoPago v){estado=v;} public LocalDateTime getTimestamp(){return timestamp;}
}

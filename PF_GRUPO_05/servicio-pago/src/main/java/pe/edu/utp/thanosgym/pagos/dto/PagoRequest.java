package pe.edu.utp.thanosgym.pagos.dto;
import jakarta.validation.constraints.*;import pe.edu.utp.thanosgym.pagos.model.MetodoPago;import java.math.BigDecimal;
public record PagoRequest(@NotNull @Positive Long idVenta,@NotNull @Positive Long idCliente,@NotNull @DecimalMin("0.01") BigDecimal monto,@NotNull MetodoPago metodoPago,@Size(max=300) String observaciones){}

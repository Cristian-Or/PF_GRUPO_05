package pe.edu.utp.thanosgym.pagos.dto;
import pe.edu.utp.thanosgym.pagos.model.*;import java.math.BigDecimal;import java.time.LocalDateTime;
public record PagoResponse(Long idPago,Long idVenta,Long idCliente,BigDecimal monto,MetodoPago metodoPago,LocalDateTime timestamp,EstadoPago estado,String comprobante,String observaciones){}

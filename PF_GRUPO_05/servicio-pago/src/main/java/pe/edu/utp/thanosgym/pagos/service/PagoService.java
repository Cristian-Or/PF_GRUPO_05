package pe.edu.utp.thanosgym.pagos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.thanosgym.pagos.dto.PagoRequest;
import pe.edu.utp.thanosgym.pagos.dto.PagoResponse;
import pe.edu.utp.thanosgym.pagos.exception.ApiException;
import pe.edu.utp.thanosgym.pagos.integration.VentaClient;
import pe.edu.utp.thanosgym.pagos.model.EstadoPago;
import pe.edu.utp.thanosgym.pagos.model.Pago;
import pe.edu.utp.thanosgym.pagos.repository.PagoRepository;

import java.util.List;

@Service
@Transactional
public class PagoService {
    private final PagoRepository repository;
    private final VentaClient ventas;
    private final String publicUrl;

    public PagoService(PagoRepository repository, VentaClient ventas,
                       @Value("${thanosgym.pagos.public-url:http://localhost:9004}") String publicUrl) {
        this.repository = repository;
        this.ventas = ventas;
        this.publicUrl = publicUrl;
    }

    public PagoResponse registrar(PagoRequest request) {
        var venta = ventas.obtener(request.idVenta());
        if (venta != null) {
            if (!"PENDIENTE_PAGO".equals(venta.estado())) {
                throw new ApiException(HttpStatus.CONFLICT, "VENTA_NO_PENDIENTE",
                        "La venta no está pendiente de pago");
            }
            if (!venta.idCliente().equals(request.idCliente())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "CLIENTE_NO_COINCIDE",
                        "La venta pertenece a otro cliente");
            }
            if (venta.montoCobrado().compareTo(request.monto()) != 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "MONTO_INVALIDO",
                        "El monto debe coincidir con el total de la venta");
            }
        }
        if (repository.existsByIdVenta(request.idVenta())) {
            throw new ApiException(HttpStatus.CONFLICT, "PAGO_DUPLICADO",
                    "La venta ya tiene un pago registrado");
        }

        Pago pago = new Pago();
        pago.setIdVenta(request.idVenta());
        pago.setIdCliente(request.idCliente());
        pago.setMonto(request.monto());
        pago.setMetodoPago(request.metodoPago());
        pago.setObservaciones(request.observaciones());
        pago.setEstado(EstadoPago.CONFIRMADO);

        repository.saveAndFlush(pago);
        ventas.confirmarPago(request.idVenta());
        return response(pago);
    }

    public PagoResponse reembolsar(Long id) {
        Pago pago = obtener(id);
        if (pago.getEstado() == EstadoPago.REEMBOLSADO) {
            return response(pago);
        }

        ventas.anular(pago.getIdVenta());
        pago.setEstado(EstadoPago.REEMBOLSADO);
        return response(repository.save(pago));
    }

    @Transactional(readOnly = true)
    public PagoResponse consultar(Long id) {
        return response(obtener(id));
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> ultimos(Long idCliente, int limit) {
        if (limit < 1 || limit > 50) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "LIMITE_INVALIDO",
                    "limit debe estar entre 1 y 50");
        }
        return repository.findByIdClienteOrderByTimestampDesc(idCliente, PageRequest.of(0, limit))
                .stream().map(this::response).toList();
    }

    @Transactional(readOnly = true)
    public String comprobante(Long id) {
        Pago pago = obtener(id);
        return "THANOSGYM\nCOMPROBANTE DE PAGO #" + pago.getIdPago()
                + "\nVenta: " + pago.getIdVenta()
                + "\nMonto: S/ " + pago.getMonto()
                + "\nMétodo: " + pago.getMetodoPago()
                + "\nEstado: " + pago.getEstado()
                + "\nFecha: " + pago.getTimestamp();
    }

    private Pago obtener(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, "PAGO_NO_ENCONTRADO", "No existe el pago " + id));
    }

    private PagoResponse response(Pago pago) {
        return new PagoResponse(
                pago.getIdPago(), pago.getIdVenta(), pago.getIdCliente(), pago.getMonto(),
                pago.getMetodoPago(), pago.getTimestamp(), pago.getEstado(),
                publicUrl + "/api/v1/pagos/" + pago.getIdPago() + "/comprobante", pago.getObservaciones());
    }
}

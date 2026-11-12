package pe.edu.utp.thanosgym.ventas.service;

import pe.edu.utp.thanosgym.ventas.dto.*;
import pe.edu.utp.thanosgym.ventas.exception.ApiException;
import pe.edu.utp.thanosgym.ventas.integration.*;
import pe.edu.utp.thanosgym.ventas.model.*;
import pe.edu.utp.thanosgym.ventas.repository.VentaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @Transactional
public class VentaService {
    private final VentaRepository repository;
    private final ClienteClient clientes;
    private final MembresiaClient membresias;

    public VentaService(VentaRepository repository, ClienteClient clientes, MembresiaClient membresias) {
        this.repository = repository;
        this.clientes = clientes;
        this.membresias = membresias;
    }

    public VentaResponse registrar(VentaRequest request) {
        clientes.verificar(request.idCliente());
        var plan = membresias.obtenerPlan(request.idPlan());

        Venta venta = new Venta();
        venta.setIdCliente(request.idCliente());
        venta.setIdPlan(plan.idPlan());
        venta.setNombrePlan(plan.nombre());
        venta.setDuracionDias(plan.duracionDias());
        venta.setFechaInicio(request.fechaInicio());
        venta.setFechaFin(request.fechaInicio().plusDays(plan.duracionDias()));
        venta.setMontoCobrado(request.montoCobrado());
        venta.setObservaciones(request.observaciones());
        venta.setEstado(EstadoVenta.PENDIENTE_PAGO);
        return response(repository.save(venta));
    }

    @Transactional(readOnly = true)
    public VentaResponse consultar(Long id) {
        return response(obtener(id));
    }

    @Transactional(readOnly = true)
    public List<VentaResponse> ultimas(Long idCliente, int limit) {
        if (limit < 1 || limit > 50) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "LIMITE_INVALIDO", "limit debe estar entre 1 y 50");
        }
        return repository.findByIdClienteOrderByFechaRegistroDesc(idCliente, PageRequest.of(0, limit))
                .stream().map(this::response).toList();
    }

    public VentaResponse confirmarPago(Long id) {
        Venta venta = obtener(id);
        if (venta.getEstado() == EstadoVenta.ANULADA) {
            throw new ApiException(HttpStatus.CONFLICT, "VENTA_ANULADA", "No se puede pagar una venta anulada");
        }
        if (venta.getEstado() == EstadoVenta.ACTIVA) {
            return response(venta);
        }
        venta.setEstado(EstadoVenta.ACTIVA);
        return response(repository.save(venta));
    }

    public VentaResponse anular(Long id) {
        Venta venta = obtener(id);
        if (venta.getEstado() == EstadoVenta.ANULADA) {
            return response(venta);
        }
        venta.setEstado(EstadoVenta.ANULADA);
        return response(repository.save(venta));
    }

    private Venta obtener(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, "VENTA_NO_ENCONTRADA", "No existe la venta " + id));
    }

    private VentaResponse response(Venta venta) {
        return new VentaResponse(
                venta.getIdVenta(), venta.getIdCliente(),
                new VentaResponse.PlanResumen(venta.getIdPlan(), venta.getNombrePlan(), venta.getDuracionDias()),
                venta.getFechaInicio(), venta.getFechaFin(), venta.getEstado(), venta.getMontoCobrado(),
                venta.getObservaciones(), venta.getFechaRegistro());
    }
}

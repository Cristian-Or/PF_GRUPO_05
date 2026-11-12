package pe.edu.utp.thanosgym.membresias.service;

import pe.edu.utp.thanosgym.membresias.dto.*;
import pe.edu.utp.thanosgym.membresias.exception.ApiException;
import pe.edu.utp.thanosgym.membresias.integration.ClienteClient;
import pe.edu.utp.thanosgym.membresias.model.*;
import pe.edu.utp.thanosgym.membresias.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service @Transactional
public class MembresiaService {
    private final PlanRepository planes; private final MembresiaRepository membresias; private final ClienteClient clientes;
    public MembresiaService(PlanRepository planes, MembresiaRepository membresias, ClienteClient clientes) {
        this.planes=planes; this.membresias=membresias; this.clientes=clientes;
    }
    @Transactional(readOnly=true) public List<PlanResponse> listarPlanes() { return planes.findByActivoTrue().stream().map(this::plan).toList(); }
    @Transactional(readOnly=true) public PlanResponse consultarPlan(String id) { return plan(obtenerPlan(id)); }
    @Transactional(readOnly=true) public MembresiaResponse consultarPorCliente(Long idCliente) {
        return response(membresias.findFirstByIdClienteOrderByFechaFinDesc(idCliente).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, "MEMBRESIA_NO_ENCONTRADA", "El cliente no tiene membresía registrada")));
    }
    public MembresiaResponse registrar(MembresiaRequest r) {
        clientes.verificar(r.idCliente()); Plan p=obtenerPlan(r.idPlan());
        Membresia m=new Membresia(); m.setIdCliente(r.idCliente()); m.setPlan(p); m.setFechaInicio(r.fechaInicio());
        m.setFechaFin(r.fechaInicio().plusDays(p.getDuracionDias())); m.setEstado(EstadoMembresia.ACTIVA);
        return response(membresias.save(m));
    }
    public MembresiaResponse actualizarEstado(Long id, EstadoMembresia estado) {
        Membresia m=obtener(id); m.setEstado(estado); return response(membresias.save(m));
    }
    @Transactional(readOnly=true) public List<MembresiaResponse> vencimientos(int dias) {
        if (dias<0 || dias>30) throw new ApiException(HttpStatus.BAD_REQUEST, "RANGO_INVALIDO", "días debe estar entre 0 y 30");
        return membresias.findByEstadoAndFechaFinLessThanEqual(EstadoMembresia.ACTIVA, LocalDate.now().plusDays(dias)).stream().map(this::response).toList();
    }
    private Plan obtenerPlan(String id) { return planes.findById(id).filter(Plan::isActivo).orElseThrow(() ->
            new ApiException(HttpStatus.NOT_FOUND, "PLAN_NO_ENCONTRADO", "El plan no existe o está inactivo")); }
    private Membresia obtener(Long id) { return membresias.findById(id).orElseThrow(() ->
            new ApiException(HttpStatus.NOT_FOUND, "MEMBRESIA_NO_ENCONTRADA", "No existe la membresía " + id)); }
    private PlanResponse plan(Plan p) { return new PlanResponse(p.getIdPlan(),p.getNombre(),p.getDuracionDias(),p.getPrecio(),p.getDescripcion(),p.isActivo()); }
    private MembresiaResponse response(Membresia m) {
        long dias=Math.max(0, ChronoUnit.DAYS.between(LocalDate.now(),m.getFechaFin()));
        return new MembresiaResponse(m.getIdMembresia(),m.getIdCliente(),plan(m.getPlan()),m.getFechaInicio(),m.getFechaFin(),dias,m.getEstado());
    }
}

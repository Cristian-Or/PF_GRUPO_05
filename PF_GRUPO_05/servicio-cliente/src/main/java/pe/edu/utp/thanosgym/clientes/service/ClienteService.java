package pe.edu.utp.thanosgym.clientes.service;

import pe.edu.utp.thanosgym.clientes.dto.*;
import pe.edu.utp.thanosgym.clientes.exception.ApiException;
import pe.edu.utp.thanosgym.clientes.integration.MembresiaClient;
import pe.edu.utp.thanosgym.clientes.model.Cliente;
import pe.edu.utp.thanosgym.clientes.model.EstadoMembresia;
import pe.edu.utp.thanosgym.clientes.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ClienteService {
    private final ClienteRepository repository;
    private final MembresiaClient membresias;

    public ClienteService(ClienteRepository repository, MembresiaClient membresias) {
        this.repository = repository;
        this.membresias = membresias;
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorDni(String dni) {
        if (dni == null || !dni.matches("\\d{8}"))
            throw new ApiException(HttpStatus.BAD_REQUEST, "DNI_INVALIDO", "El DNI debe tener exactamente 8 dígitos");
        return toResponseConMembresia(repository.findByDni(dni).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, "CLIENTE_NO_ENCONTRADO", "No existe un cliente con DNI " + dni)));
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) { return toResponseConMembresia(obtener(id)); }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar(String nombre) {
        List<Cliente> clientes = nombre == null || nombre.isBlank() ? repository.findAll()
                : repository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(nombre, nombre);
        return clientes.stream().map(this::toResponse).toList();
    }

    public ClienteResponse registrar(ClienteRequest request) {
        if (repository.existsByDni(request.dni()))
            throw new ApiException(HttpStatus.CONFLICT, "DNI_DUPLICADO", "Ya existe un cliente con DNI " + request.dni());
        Cliente c = new Cliente(); map(request, c);
        return toResponse(repository.save(c));
    }

    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        Cliente c = obtener(id);
        repository.findByDni(request.dni()).filter(x -> !x.getIdCliente().equals(id)).ifPresent(x -> {
            throw new ApiException(HttpStatus.CONFLICT, "DNI_DUPLICADO", "El DNI pertenece a otro cliente");
        });
        map(request, c); return toResponse(repository.save(c));
    }

    public void eliminar(Long id) { repository.delete(obtener(id)); }

    private Cliente obtener(Long id) { return repository.findById(id).orElseThrow(() ->
            new ApiException(HttpStatus.NOT_FOUND, "CLIENTE_NO_ENCONTRADO", "No existe el cliente " + id)); }
    private void map(ClienteRequest r, Cliente c) {
        c.setDni(r.dni()); c.setNombres(r.nombres().trim()); c.setApellidos(r.apellidos().trim());
        c.setTelefono(r.telefono()); c.setEmail(r.email() == null || r.email().isBlank() ? null : r.email());
        c.setFechaNacimiento(r.fechaNacimiento()); c.setPlan(r.plan());
    }
    private ClienteResponse toResponse(Cliente c) {
        return new ClienteResponse(c.getIdCliente(), c.getDni(), c.getNombres(), c.getApellidos(), c.getTelefono(),
                c.getEmail(), c.getFechaNacimiento(), c.getPlan(), c.getFechaRegistro(), c.getEstadoMembresia(), c.getIdMembresia());
    }

    private ClienteResponse toResponseConMembresia(Cliente c) {
        var membresia = membresias.buscarPorCliente(c.getIdCliente());
        if (membresia.isEmpty()) {
            return toResponse(c);
        }

        var resumen = membresia.get();
        EstadoMembresia estado;
        try {
            estado = EstadoMembresia.valueOf(resumen.estado());
        } catch (IllegalArgumentException | NullPointerException ex) {
            return toResponse(c);
        }

        return new ClienteResponse(c.getIdCliente(), c.getDni(), c.getNombres(), c.getApellidos(), c.getTelefono(),
                c.getEmail(), c.getFechaNacimiento(), c.getPlan(), c.getFechaRegistro(), estado, resumen.idMembresia());
    }
}

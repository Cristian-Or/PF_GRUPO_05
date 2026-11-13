package pe.edu.utp.thanosgym.clientes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import pe.edu.utp.thanosgym.clientes.dto.*;
import pe.edu.utp.thanosgym.clientes.service.ClienteService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Clientes")
public class ClienteController {
    private final ClienteService service;
    public ClienteController(ClienteService service) { this.service = service; }

    @GetMapping @Operation(summary = "Ver clientes")
    List<ClienteResponse> listar(@RequestParam(required = false) String nombre) { return service.listar(nombre); }
    @GetMapping("/{id}") @Operation(summary = "Buscar cliente por ID")
    ClienteResponse buscarPorId(@PathVariable Long id) { return service.buscarPorId(id); }
    @GetMapping("/dni/{dni}") @Operation(summary = "Buscar cliente por DNI")
    ClienteResponse buscarPorDni(@PathVariable String dni) { return service.buscarPorDni(dni); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) @Operation(summary = "Registrar cliente")
    ClienteResponse registrar(@Valid @RequestBody ClienteRequest request) { return service.registrar(request); }
    @PutMapping("/{id}") @Operation(summary = "Actualizar cliente")
    ClienteResponse actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest request) { return service.actualizar(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @Operation(summary = "Eliminar cliente")
    void eliminar(@PathVariable Long id) { service.eliminar(id); }
}

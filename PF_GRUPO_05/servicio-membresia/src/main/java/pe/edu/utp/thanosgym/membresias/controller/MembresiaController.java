package pe.edu.utp.thanosgym.membresias.controller;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import pe.edu.utp.thanosgym.membresias.dto.*;
import pe.edu.utp.thanosgym.membresias.service.MembresiaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/v1/membresias") @SecurityRequirement(name="bearerAuth")
@Tag(name="Membresías")
public class MembresiaController {
    private final MembresiaService service;
    public MembresiaController(MembresiaService service) { this.service=service; }
    @GetMapping("/planes") @Operation(summary="Ver planes disponibles") List<PlanResponse> planes() { return service.listarPlanes(); }
    @GetMapping("/planes/{idPlan}") @Operation(summary="Buscar plan") PlanResponse plan(@PathVariable String idPlan) { return service.consultarPlan(idPlan); }
    @GetMapping("/cliente/{idCliente}") @Operation(summary="Ver membresía de un cliente") MembresiaResponse cliente(@PathVariable Long idCliente) { return service.consultarPorCliente(idCliente); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) @Operation(summary="Registrar membresía")
    MembresiaResponse registrar(@Valid @RequestBody MembresiaRequest request) { return service.registrar(request); }
    @PatchMapping("/{id}/estado") @Operation(summary="Cambiar estado de la membresía")
    MembresiaResponse estado(@PathVariable Long id, @Valid @RequestBody EstadoRequest request) { return service.actualizarEstado(id,request.estado()); }
    @GetMapping("/vencimientos") @Operation(summary="Ver próximos vencimientos")
    List<MembresiaResponse> vencimientos(@RequestParam(defaultValue="3") int dias) { return service.vencimientos(dias); }
}

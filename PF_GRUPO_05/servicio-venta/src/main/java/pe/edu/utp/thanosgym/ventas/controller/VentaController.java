package pe.edu.utp.thanosgym.ventas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import pe.edu.utp.thanosgym.ventas.dto.*;
import pe.edu.utp.thanosgym.ventas.service.VentaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/v1/ventas") @SecurityRequirement(name="bearerAuth")
@Tag(name="Ventas")
public class VentaController {
    private final VentaService service; public VentaController(VentaService s){service=s;}
    @PostMapping @ResponseStatus(HttpStatus.CREATED) @Operation(summary="Registrar una venta") VentaResponse registrar(@Valid @RequestBody VentaRequest r){return service.registrar(r);}
    @GetMapping("/{id}") @Operation(summary="Buscar venta") VentaResponse consultar(@PathVariable Long id){return service.consultar(id);}
    @GetMapping("/cliente/{idCliente}") @Operation(summary="Ver ventas de un cliente") List<VentaResponse> ultimas(@PathVariable Long idCliente,@RequestParam(defaultValue="3") int limit){return service.ultimas(idCliente,limit);}
    @PatchMapping("/{id}/confirmar-pago") @Operation(summary="Activar una venta después de confirmar su pago") VentaResponse confirmarPago(@PathVariable Long id){return service.confirmarPago(id);}
    @PatchMapping("/{id}/anular") @Operation(summary="Anular venta") VentaResponse anular(@PathVariable Long id){return service.anular(id);}
}

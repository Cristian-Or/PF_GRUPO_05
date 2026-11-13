package pe.edu.utp.thanosgym.pagos.controller;
import io.swagger.v3.oas.annotations.Operation;import io.swagger.v3.oas.annotations.security.SecurityRequirement;import jakarta.validation.Valid;import pe.edu.utp.thanosgym.pagos.dto.*;import pe.edu.utp.thanosgym.pagos.service.PagoService;import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController @RequestMapping("/api/v1/pagos") @SecurityRequirement(name="bearerAuth") @Tag(name="Pagos") public class PagoController{
 private final PagoService service;public PagoController(PagoService s){service=s;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) @Operation(summary="Registrar un pago") PagoResponse registrar(@Valid @RequestBody PagoRequest r){return service.registrar(r);}
 @GetMapping("/{id}") @Operation(summary="Buscar pago") PagoResponse consultar(@PathVariable Long id){return service.consultar(id);}
 @GetMapping("/cliente/{idCliente}") @Operation(summary="Ver pagos de un cliente") List<PagoResponse> ultimos(@PathVariable Long idCliente,@RequestParam(defaultValue="3") int limit){return service.ultimos(idCliente,limit);}
 @GetMapping(value="/{id}/comprobante",produces=MediaType.TEXT_PLAIN_VALUE) @Operation(summary="Ver comprobante") String comprobante(@PathVariable Long id){return service.comprobante(id);}
 @PostMapping("/{id}/reembolsar") @Operation(summary="Reembolsar un pago y anular su venta") PagoResponse reembolsar(@PathVariable Long id){return service.reembolsar(id);}
}

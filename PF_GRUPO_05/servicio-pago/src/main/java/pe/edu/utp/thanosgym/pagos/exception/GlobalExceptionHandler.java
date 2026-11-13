package pe.edu.utp.thanosgym.pagos.exception;
import jakarta.servlet.http.HttpServletRequest;import org.springframework.http.*;import org.springframework.web.bind.MethodArgumentNotValidException;import org.springframework.web.bind.annotation.*;import java.net.URI;import java.util.stream.Collectors;
@RestControllerAdvice public class GlobalExceptionHandler{
 @ExceptionHandler(ApiException.class) ResponseEntity<ProblemDetail> api(ApiException ex,HttpServletRequest req){ProblemDetail p=ProblemDetail.forStatusAndDetail(ex.getStatus(),ex.getMessage());p.setTitle(ex.getCode());p.setProperty("codigo",ex.getCode());p.setInstance(URI.create(req.getRequestURI()));return ResponseEntity.status(ex.getStatus()).body(p);}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ProblemDetail> val(MethodArgumentNotValidException ex){String d=ex.getBindingResult().getFieldErrors().stream().map(e->e.getField()+": "+e.getDefaultMessage()).collect(Collectors.joining("; "));ProblemDetail p=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,d);p.setTitle("DATOS_INVALIDOS");p.setProperty("codigo","DATOS_INVALIDOS");return ResponseEntity.badRequest().body(p);}
}

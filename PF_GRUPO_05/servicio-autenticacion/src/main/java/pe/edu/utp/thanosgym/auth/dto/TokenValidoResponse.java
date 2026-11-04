package pe.edu.utp.thanosgym.auth.dto;import pe.edu.utp.thanosgym.auth.model.Rol;import java.time.Instant;
public record TokenValidoResponse(boolean valido,Long idUsuario,Rol rol,Instant expiracion){}

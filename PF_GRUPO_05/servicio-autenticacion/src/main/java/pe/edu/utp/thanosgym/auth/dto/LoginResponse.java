package pe.edu.utp.thanosgym.auth.dto;import pe.edu.utp.thanosgym.auth.model.Rol;
public record LoginResponse(String token,long expiresIn,Rol rol,String nombre){}

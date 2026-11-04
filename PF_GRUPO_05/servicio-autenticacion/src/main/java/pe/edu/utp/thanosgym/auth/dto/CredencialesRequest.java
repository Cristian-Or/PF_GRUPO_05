package pe.edu.utp.thanosgym.auth.dto;import jakarta.validation.constraints.*;
public record CredencialesRequest(@NotBlank @Size(max=50) String usuario,@NotBlank @Size(max=100) String password){}

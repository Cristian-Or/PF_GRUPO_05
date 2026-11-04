package pe.edu.utp.thanosgym.auth.service;
import io.jsonwebtoken.*;import io.jsonwebtoken.io.Decoders;import io.jsonwebtoken.security.Keys;import pe.edu.utp.thanosgym.auth.exception.ApiException;import pe.edu.utp.thanosgym.auth.model.*;import org.springframework.beans.factory.annotation.Value;import org.springframework.http.HttpStatus;import org.springframework.stereotype.Service;import javax.crypto.SecretKey;import java.time.*;import java.util.*;
@Service public class JwtService{
 private final SecretKey key;private final long expirationSeconds;public JwtService(@Value("${thanosgym.jwt.secret}") String secret,@Value("${thanosgym.jwt.expiration-seconds:28800}") long exp){key=Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));expirationSeconds=exp;}
 public String generar(Usuario u){Instant now=Instant.now();return Jwts.builder().subject(u.getUsuario()).id(UUID.randomUUID().toString()).claim("idUsuario",u.getIdUsuario()).claim("rol",u.getRol().name()).issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(expirationSeconds))).signWith(key).compact();}
 public Claims validar(String token){try{return Jwts.parser().verifyWith(key).build().parseSignedClaims(limpiar(token)).getPayload();}catch(ExpiredJwtException ex){throw new ApiException(HttpStatus.UNAUTHORIZED,"TOKEN_EXPIRADO","El token ha vencido");}catch(JwtException|IllegalArgumentException ex){throw new ApiException(HttpStatus.UNAUTHORIZED,"TOKEN_INVALIDO","El token no es válido");}}
 public String limpiar(String token){if(token==null||token.isBlank())throw new ApiException(HttpStatus.UNAUTHORIZED,"TOKEN_REQUERIDO","Debe enviar Authorization: Bearer <token>");return token.startsWith("Bearer ")?token.substring(7):token;}
 public long expirationSeconds(){return expirationSeconds;}
}

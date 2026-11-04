package pe.edu.utp.thanosgym.auth.config;
import pe.edu.utp.thanosgym.auth.model.*;import pe.edu.utp.thanosgym.auth.repository.UsuarioRepository;import org.springframework.boot.CommandLineRunner;import org.springframework.context.annotation.*;import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration public class UsuarioDataLoader{
 @Bean CommandLineRunner usuariosIniciales(UsuarioRepository r,PasswordEncoder e){return args->{if(r.count()==0){Usuario admin=usuario("admin","Admin ThanosGym",Rol.ADMIN,e.encode("Admin123*"));Usuario recepcion=usuario("recepcion","Recepción ThanosGym",Rol.RECEPCIONISTA,e.encode("Recepcion123*"));r.save(admin);r.save(recepcion);}};}
 private Usuario usuario(String login,String nombre,Rol rol,String hash){Usuario u=new Usuario();u.setUsuario(login);u.setNombre(nombre);u.setRol(rol);u.setPasswordHash(hash);u.setActivo(true);return u;}
}

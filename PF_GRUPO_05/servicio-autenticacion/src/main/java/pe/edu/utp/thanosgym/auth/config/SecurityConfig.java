package pe.edu.utp.thanosgym.auth.config;
import org.springframework.context.annotation.*;import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration public class SecurityConfig{@Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}}

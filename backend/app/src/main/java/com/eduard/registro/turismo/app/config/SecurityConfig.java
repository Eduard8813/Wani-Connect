package com.eduard.registro.turismo.app.config;

// Importa el filtro personalizado que valida JWT en cada solicitud
import com.eduard.registro.turismo.app.security.JwtRequestFilter;

// Lombok genera constructor con los atributos marcados como final
import lombok.RequiredArgsConstructor;

// Anotaciones para definir configuración de seguridad en Spring Boot
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Clase que configura la seguridad de la aplicación.
 * Define reglas de acceso, autenticación, manejo de sesiones y filtros personalizados.
 */
@Configuration // Registra esta clase como configuración en el contexto de Spring
@EnableWebSecurity // Habilita seguridad web en Spring
@EnableMethodSecurity // Permite usar anotaciones como @PreAuthorize en métodos
@RequiredArgsConstructor // Lombok genera constructor con los atributos final
public class SecurityConfig {

    // Servicio que carga los detalles del usuario desde la base de datos o fuente personalizada
    private final UserDetailsService userDetailsService;

    // Filtro que intercepta las solicitudes para validar el token JWT
    private final JwtRequestFilter jwtRequestFilter;

    /**
     * Configura la cadena de filtros de seguridad.
     * Define qué rutas son públicas, desactiva CSRF y sesiones, e integra el filtro JWT.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Desactiva protección CSRF (no necesaria en APIs REST)
           // .cors(cors -> cors.disable()) // Desactiva CORS si ya lo configuras en CorsConfig
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Permite solicitudes preflight CORS
                .requestMatchers("/api/auth/**").permitAll() // Permite acceso público a rutas de autenticación
                .requestMatchers("/api/user/**").permitAll() // Permite acceso público a rutas de usuario
                .requestMatchers(HttpMethod.POST, "/api/fotos/subir").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/fotos/**").permitAll()
                .anyRequest().authenticated() // Requiere autenticación para cualquier otra ruta
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No se guarda sesión en el servidor
            );

        // Agrega el filtro JWT antes del filtro estándar de autenticación por usuario/contraseña
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Bean que define el encoder de contraseñas.
     * Se usa para encriptar y verificar contraseñas de usuarios.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Algoritmo seguro y ampliamente usado
    }

    /**
     * Bean que configura el AuthenticationManager.
     * Usa el servicio de usuarios y el encoder para validar credenciales.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return auth.build();
    }
}

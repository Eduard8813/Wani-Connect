package com.eduard.registro.turismo.app.security;

// Utilidades para validar y extraer información del token JWT
import com.eduard.registro.turismo.app.security.JwtTokenUtil;
// Clases necesarias para construir el filtro
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// Lombok genera constructor con los atributos marcados como final
import lombok.RequiredArgsConstructor;
// Clases de Spring Security para autenticación y contexto
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// Marca esta clase como componente para que Spring la detecte
import org.springframework.stereotype.Component;
// Filtro que se ejecuta una vez por solicitud
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Filtro que intercepta cada solicitud HTTP para validar el token JWT.
 * Si el token es válido, establece el contexto de seguridad con el usuario autenticado.
 */
@Component // Registra el filtro como componente en el contexto de Spring
@RequiredArgsConstructor // Lombok genera constructor con los atributos final
public class JwtRequestFilter extends OncePerRequestFilter {
    // Servicio que carga los detalles del usuario desde la base de datos
    private final UserDetailsServiceImpl userDetailsService;
    // Utilidad para extraer y validar el token JWT
    private final JwtTokenUtil jwtTokenUtil;
    
    /**
     * Método que se ejecuta en cada solicitud HTTP.
     * Extrae el token, lo valida y establece el contexto de seguridad si es válido.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain) throws ServletException, IOException {
        
        // Extraer el encabezado Authorization
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        
        // Verificar si el encabezado contiene un token Bearer
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Extraer el token sin el prefijo "Bearer "
            try {
                username = jwtTokenUtil.extractUsername(jwt); // Extraer el nombre de usuario del token
            } catch (Exception e) {
                // Manejo de error si el token es inválido o expirado
                logger.error("Unable to get JWT Token", e);
                // Enviar respuesta de error inmediatamente
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token JWT inválido");
                return;
            }
        }
        
        // Verificar si el usuario fue extraído y aún no está autenticado
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            
            // Validar el token con los detalles del usuario
            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                // Crear el objeto de autenticación
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                // Asociar detalles de la solicitud (IP, sesión, etc.)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Establecer el usuario autenticado en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        
        // Continuar con el siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }
}
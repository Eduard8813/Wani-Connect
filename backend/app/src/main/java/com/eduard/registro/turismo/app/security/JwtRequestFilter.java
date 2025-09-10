package com.eduard.registro.turismo.app.security;

import com.eduard.registro.turismo.app.security.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

  @Override
protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain) throws ServletException, IOException {
    
    final String authorizationHeader = request.getHeader("Authorization");
    String username = null;
    String jwt = null;
    
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        jwt = authorizationHeader.substring(7);
        
        // Manejar tokens que puedan tener prefijos adicionales
        if (jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
            log.warn("Se detectó un token con doble prefijo Bearer, se corrigió automáticamente");
        }
        
        try {
            username = jwtTokenUtil.extractUsername(jwt);
            log.debug("Username extraído del JWT: {}", username);
        } catch (Exception e) {
            log.error("No se pudo obtener el username del JWT: {}", e.getMessage());
            log.debug("Token inválido: {}", jwt);
        }
    } else {
        log.warn("El token JWT no comienza con Bearer String o es nulo");
    }
    
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        try {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            log.debug("UserDetails cargados para: {}", username);
            
            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                log.debug("Token JWT válido para el usuario: {}", username);
                
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                log.debug("Autenticación establecida para el usuario: {} con autoridades: {}", 
                        username, userDetails.getAuthorities());
            } else {
                log.warn("Validación del token JWT fallida para el usuario: {}", username);
            }
        } catch (Exception e) {
            log.error("Error durante la autenticación del usuario {}: {}", username, e.getMessage());
        }
    } else {
        if (username != null) {
            log.debug("El usuario {} ya está autenticado", username);
        }
    }
    
    filterChain.doFilter(request, response);
}
}
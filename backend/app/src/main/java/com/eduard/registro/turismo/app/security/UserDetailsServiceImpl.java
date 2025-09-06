package com.eduard.registro.turismo.app.security;

// Lombok genera constructor con los atributos marcados como final
import lombok.RequiredArgsConstructor;

// Interfaces y clases de Spring Security para autenticación
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// Marca esta clase como servicio para que Spring la detecte
import org.springframework.stereotype.Service;

// Entidad del modelo de usuario
import com.eduard.registro.turismo.app.model.User;

// Repositorio para acceder a los datos del usuario
import com.eduard.registro.turismo.app.repository.UserRepository;

import java.util.Collections;

/**
 * Servicio que implementa la interfaz UserDetailsService de Spring Security.
 * Se encarga de cargar los datos del usuario desde la base de datos para el proceso de autenticación.
 */
@Service // Registra esta clase como servicio en el contexto de Spring
@RequiredArgsConstructor // Lombok genera constructor con los atributos final
public class UserDetailsServiceImpl implements UserDetailsService {

    // Repositorio para buscar usuarios por nombre
    private final UserRepository userRepository;

    /**
     * Método que carga los datos del usuario por su nombre.
     * Si el usuario existe, devuelve un objeto UserDetails con sus credenciales y rol.
     * Si no existe, lanza una excepción que detiene la autenticación.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar el usuario por su nombre
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Construir el objeto UserDetails con el rol "ROLE_USER"
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), // Nombre de usuario
                user.getPassword(), // Contraseña encriptada
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // Lista de roles
        );
    }
}

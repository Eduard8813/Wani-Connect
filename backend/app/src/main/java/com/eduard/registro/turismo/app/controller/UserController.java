package com.eduard.registro.turismo.app.controller;

// Lombok genera constructor con los atributos marcados como final
import lombok.RequiredArgsConstructor;

// Utilidades para construir respuestas HTTP
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// Objeto que representa al usuario autenticado
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// Anotaciones para definir controlador REST y rutas
import org.springframework.web.bind.annotation.*;

// Entidad del modelo que representa el perfil del usuario
import com.eduard.registro.turismo.app.model.UserProfile;

// Servicio que gestiona la lógica de negocio del perfil
import com.eduard.registro.turismo.app.service.UserProfileService;

// Repositorio para acceder a los datos del usuario
import com.eduard.registro.turismo.app.repository.UserRepository;

/**
 * Controlador REST que gestiona operaciones relacionadas con el usuario autenticado.
 * Expone un endpoint para recuperar el perfil del usuario actual.
 */
@RestController // Define esta clase como controlador REST
@RequestMapping("/api/user") // Prefijo común para rutas relacionadas con el usuario
@RequiredArgsConstructor // Lombok genera constructor con los atributos final
public class UserController {

    // Servicio para acceder al perfil del usuario
    private final UserProfileService profileService;

    // Repositorio para acceder a los datos del usuario
    private final UserRepository userRepository;

    /**
     * Endpoint protegido que devuelve el perfil del usuario autenticado.
     * Requiere un token JWT válido para acceder.
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        // Verificar si el usuario está autenticado
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Error: No autorizado - Token no proporcionado o inválido");
        }

        try {
            // Obtener el username del usuario autenticado desde el token
            String username = authentication.getName();
            
            // Buscar el usuario por username para obtener su ID
            Long userId = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username))
                    .getId();
            
            // Obtener el perfil por el ID del usuario
            UserProfile profile = profileService.getProfileByUserId(userId);
            
            // Devolver el perfil como respuesta
            return ResponseEntity.ok(profile);
            
        } catch (UsernameNotFoundException e) {
            // Manejo de error si el usuario no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            // Manejo de error general
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener el perfil: " + e.getMessage());
        }
    }
}

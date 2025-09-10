package com.eduard.registro.turismo.app.controller;

// Lombok genera constructor con los atributos marcados como final
import lombok.RequiredArgsConstructor;

// Utilidades para construir respuestas HTTP
import org.springframework.http.ResponseEntity;

// Clases de Spring Security para autenticación
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
// Anotaciones para definir controlador REST y rutas
import org.springframework.web.bind.annotation.*;

// DTOs para solicitudes y respuestas de autenticación
import com.eduard.registro.turismo.app.dto.AuthRequest;
import com.eduard.registro.turismo.app.dto.AuthResponse;
import com.eduard.registro.turismo.app.dto.SignUpRequest;

// Entidades del modelo
import com.eduard.registro.turismo.app.model.User;
import com.eduard.registro.turismo.app.model.UserProfile;

// Utilidades de seguridad para generar y validar JWT
import com.eduard.registro.turismo.app.security.JwtTokenUtil;
import com.eduard.registro.turismo.app.security.UserDetailsServiceImpl;

// Servicios para manejar lógica de negocio
import com.eduard.registro.turismo.app.service.UserProfileService;
import com.eduard.registro.turismo.app.service.UserService;

// Validación de datos de entrada
import jakarta.validation.Valid;

/**
 * Controlador REST que gestiona la autenticación y el registro de usuarios.
 * Expone endpoints públicos para iniciar sesión y crear nuevos usuarios.
 */
@RestController // Define esta clase como controlador REST
@RequestMapping("/api/auth") // Prefijo común para todas las rutas de autenticación
@RequiredArgsConstructor // Lombok genera constructor con los atributos final
public class AuthController {

    // Inyección de dependencias necesarias para autenticación y registro
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserProfileService profileService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * Endpoint para iniciar sesión.
     * Valida las credenciales y genera un token JWT si son correctas.
     */
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
        // Autentica al usuario usando su nombre y contraseña
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        
        // Carga los detalles del usuario y genera el token JWT
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        
        // Devuelve el token en la respuesta
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    /**
     * Endpoint para registrar un nuevo usuario.
     * Valida que el nombre de usuario y el email no estén en uso,
     * luego crea el usuario y su perfil asociado.
     */
    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            // Validación de unicidad de nombre de usuario
            if (userService.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.badRequest().body("Error: El nombre de usuario ya existe");
        }
        
        // Validación de unicidad de email
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: El email ya está en uso");
        }
        
        // Crear entidad User con los datos recibidos
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(signUpRequest.getPassword());
        user.setEmail(signUpRequest.getEmail());
        User newUser = userService.createUser(user);
        
        // Crear entidad UserProfile asociada al nuevo usuario
        UserProfile profile = new UserProfile();
        profile.setFirstName(signUpRequest.getFirstName());
        profile.setLastName(signUpRequest.getLastName());
        profile.setPhone(signUpRequest.getPhone());
        profile.setAddress(signUpRequest.getAddress());
        profile.setBirthDate(signUpRequest.getBirthDate());
        profile.setGender(signUpRequest.getGender());
        profile.setLocation(signUpRequest.getLocation());
        profile.setCountryOfOrigin(signUpRequest.getCountryOfOrigin());
        profile.setLanguage(signUpRequest.getLanguage());
        profile.setTouristInterest(signUpRequest.getTouristInterest());
        profile.setSocial(signUpRequest.getSocial());
        profile.setDescription(signUpRequest.getDescription());
        
        profile.setUser(newUser);
        profileService.createProfile(profile);
        
        return ResponseEntity.ok("Usuario registrado exitosamente");
    } catch (Exception e) {
        return ResponseEntity.internalServerError().body("Error al registrar usuario: " + e.getMessage());
    }
}

}
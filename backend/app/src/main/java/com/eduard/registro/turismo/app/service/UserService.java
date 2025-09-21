package com.eduard.registro.turismo.app.service;

// Lombok genera constructor con los atributos marcados como final
import lombok.RequiredArgsConstructor;

// Servicio de Spring para registrar esta clase como bean
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Entidad del modelo de usuario
import com.eduard.registro.turismo.app.model.User;

// Repositorio para acceder a los datos del usuario
import com.eduard.registro.turismo.app.repository.UserRepository;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los usuarios.
 * Permite crear usuarios con contraseñas seguras y validar unicidad de datos.
 */
@Service // Registra esta clase como servicio en el contexto de Spring
@RequiredArgsConstructor // Lombok genera constructor con los atributos final
public class UserService {

    // Repositorio para persistencia de usuarios
    private final UserRepository userRepository;

    // Encoder para encriptar contraseñas antes de guardarlas
    private final PasswordEncoder passwordEncoder;

    /**
     * Crea un nuevo usuario en la base de datos.
     * Encripta la contraseña antes de guardarla.
     *
     * @param user objeto User con los datos del nuevo usuario
     * @return usuario guardado
     */
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encripta la contraseña
        return userRepository.save(user); // Guarda el usuario en la base de datos
    }

    /**
     * Verifica si ya existe un usuario con el nombre de usuario proporcionado.
     *
     * @param username nombre de usuario
     * @return true si existe, false si no
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Verifica si ya existe un usuario con el correo electrónico proporcionado.
     *
     * @param email correo electrónico
     * @return true si existe, false si no
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario
     * @return Optional con el usuario si existe
     */
    public java.util.Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Guarda un usuario existente.
     *
     * @param user usuario a guardar
     * @return usuario guardado
     */
    public User save(User user) {
        return userRepository.save(user);
    }
}

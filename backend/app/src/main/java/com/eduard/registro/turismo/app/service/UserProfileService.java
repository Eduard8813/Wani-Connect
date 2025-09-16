package com.eduard.registro.turismo.app.service;

// Importa la entidad del perfil de usuario
import com.eduard.registro.turismo.app.model.UserProfile;

// Importa el repositorio que gestiona el acceso a datos del perfil
import com.eduard.registro.turismo.app.repository.UserProfileRepository;

// Lombok genera constructor con los atributos marcados como final
import lombok.RequiredArgsConstructor;

// Marca esta clase como servicio para que Spring la detecte
import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona la lógica de negocio relacionada con el perfil del usuario.
 * Permite crear nuevos perfiles y recuperar perfiles existentes por ID de usuario.
 */
@Service // Registra esta clase como servicio en el contexto de Spring
@RequiredArgsConstructor // Lombok genera constructor con los atributos final
public class UserProfileService {

    // Repositorio para acceder a los datos del perfil
    private final UserProfileRepository profileRepository;

    /**
     * Crea y guarda un nuevo perfil de usuario en la base de datos.
     *
     * @param profile objeto UserProfile con los datos del perfil
     * @return perfil guardado
     */
    public UserProfile createProfile(UserProfile profile) {
        return profileRepository.save(profile);
    }

    /**
     * Recupera el perfil de usuario asociado al ID proporcionado.
     * Lanza una excepción si no se encuentra el perfil.
     *
     * @param userId ID del usuario
     * @return perfil asociado al usuario
     */
    public UserProfile getProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado para el usuario: " + userId));
    }
}

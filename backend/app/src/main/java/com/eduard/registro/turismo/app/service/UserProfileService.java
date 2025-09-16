package com.eduard.registro.turismo.app.service;

// Importa la entidad del perfil de usuario
import com.eduard.registro.turismo.app.model.UserProfile;
import com.eduard.registro.turismo.app.model.User;

// Importa el repositorio que gestiona el acceso a datos del perfil
import com.eduard.registro.turismo.app.repository.UserProfileRepository;
import com.eduard.registro.turismo.app.repository.UserRepository;

// Importa los DTOs
import com.eduard.registro.turismo.app.dto.UserProfileResponse;
import com.eduard.registro.turismo.app.dto.UserProfileUpdateRequest;

// Lombok genera constructor con los atributos marcados como final
import lombok.RequiredArgsConstructor;

// Marca esta clase como servicio para que Spring la detecte
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio que gestiona la lógica de negocio relacionada con el perfil del usuario.
 * Permite crear nuevos perfiles, recuperar perfiles existentes y actualizarlos.
 */
@Service // Registra esta clase como servicio en el contexto de Spring
@RequiredArgsConstructor // Lombok genera constructor con los atributos final
public class UserProfileService {

    // Repositorio para acceder a los datos del perfil
    private final UserProfileRepository profileRepository;
    
    // Repositorio para acceder a los datos del usuario
    private final UserRepository userRepository;

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

    /**
     * Recupera el perfil de usuario con información del usuario como DTO.
     *
     * @param userId ID del usuario
     * @return UserProfileResponse con los datos del perfil y usuario
     */
    public UserProfileResponse getProfileResponseByUserId(Long userId) {
        UserProfile profile = getProfileByUserId(userId);
        User user = profile.getUser();
        
        UserProfileResponse response = new UserProfileResponse();
        response.setId(profile.getId());
        response.setFirstName(profile.getFirstName());
        response.setLastName(profile.getLastName());
        response.setPhone(profile.getPhone());
        response.setAddress(profile.getAddress());
        response.setBirthDate(profile.getBirthDate());
        response.setGender(profile.getGender());
        response.setLocation(profile.getLocation());
        response.setCountryOfOrigin(profile.getCountryOfOrigin());
        response.setLanguage(profile.getLanguage());
        response.setTouristInterest(profile.getTouristInterest());
        response.setSocial(profile.getSocial());
        response.setDescription(profile.getDescription());
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        
        return response;
    }

    /**
     * Actualiza el perfil de usuario con los nuevos datos proporcionados.
     *
     * @param userId ID del usuario
     * @param updateRequest datos actualizados del perfil
     * @return UserProfileResponse con los datos actualizados
     */
    @Transactional
    public UserProfileResponse updateProfile(Long userId, UserProfileUpdateRequest updateRequest) {
        UserProfile profile = getProfileByUserId(userId);
        
        // Actualizar los campos del perfil
        profile.setFirstName(updateRequest.getFirstName());
        profile.setLastName(updateRequest.getLastName());
        profile.setPhone(updateRequest.getPhone());
        profile.setAddress(updateRequest.getAddress());
        profile.setBirthDate(updateRequest.getBirthDate());
        profile.setGender(updateRequest.getGender());
        profile.setLocation(updateRequest.getLocation());
        profile.setCountryOfOrigin(updateRequest.getCountryOfOrigin());
        profile.setLanguage(updateRequest.getLanguage());
        profile.setTouristInterest(updateRequest.getTouristInterest());
        profile.setSocial(updateRequest.getSocial());
        profile.setDescription(updateRequest.getDescription());
        
        // Guardar los cambios
        UserProfile updatedProfile = profileRepository.save(profile);
        
        // Retornar como DTO
        return getProfileResponseByUserId(userId);
    }
}
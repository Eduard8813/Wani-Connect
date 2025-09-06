package com.eduard.registro.turismo.app.repository;

// Importa la entidad UserProfile
import com.eduard.registro.turismo.app.model.UserProfile;

// Importa JpaRepository para heredar operaciones CRUD
import org.springframework.data.jpa.repository.JpaRepository;

// Importa Optional para manejar resultados que pueden estar vacíos
import java.util.Optional;

/**
 * Repositorio que gestiona el acceso a datos de la entidad UserProfile.
 * Extiende JpaRepository para heredar métodos CRUD y define una consulta personalizada.
 */
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    /**
     * Método personalizado que busca un perfil por el ID del usuario asociado.
     * Utiliza el nombre del método para generar la consulta automáticamente.
     *
     * @param userId ID del usuario
     * @return Optional con el perfil si existe, vacío si no
     */
    Optional<UserProfile> findByUserId(Long userId);
}

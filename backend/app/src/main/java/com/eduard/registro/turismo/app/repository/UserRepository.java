package com.eduard.registro.turismo.app.repository;

// Importa JpaRepository para heredar operaciones CRUD
import org.springframework.data.jpa.repository.JpaRepository;

// Importa la entidad User
import com.eduard.registro.turismo.app.model.User;

// Importa Optional para manejar resultados que pueden estar vacíos
import java.util.Optional;

/**
 * Repositorio que gestiona el acceso a datos de la entidad User.
 * Extiende JpaRepository para heredar métodos CRUD y define consultas personalizadas.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     * Utiliza convención de nombres para generar la consulta automáticamente.
     *
     * @param username nombre de usuario
     * @return Optional con el usuario si existe, vacío si no
     */
    Optional<User> findByUsername(String username);

    /**
     * Verifica si existe un usuario con el nombre de usuario proporcionado.
     *
     * @param username nombre de usuario
     * @return true si existe, false si no
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si existe un usuario con el correo electrónico proporcionado.
     *
     * @param email correo electrónico
     * @return true si existe, false si no
     */
    boolean existsByEmail(String email);
}

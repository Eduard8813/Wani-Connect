package com.eduard.registro.turismo.app.model;

// Lombok genera automáticamente getters, setters, toString, equals y hashCode
import lombok.Data;

// Anotaciones JPA para definir la persistencia de la entidad
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entidad que representa a un usuario en el sistema.
 * Se mapea a la tabla "users" y contiene datos básicos de autenticación y contacto.
 */
@Data // Lombok genera automáticamente los métodos necesarios para esta clase
@Entity // Marca esta clase como una entidad JPA
@Table(name = "users") // Define el nombre de la tabla en la base de datos
public class User {

    @Id // Marca este campo como clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera el ID automáticamente usando la estrategia de la base de datos
    private Long id;

    @Column(unique = true, nullable = false) // El nombre de usuario debe ser único y obligatorio
    private String username;

    @Column(nullable = false) // La contraseña es obligatoria
    private String password;

    @Column(unique = true, nullable = false) // El email debe ser único y obligatorio
    private String email;

    @Column(nullable = false) // Fecha de creación obligatoria
    private LocalDateTime createdAt = LocalDateTime.now(); // Se inicializa con la fecha y hora actual

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Relación uno a uno con UserProfile, con cascada total
    private UserProfile profile;
}

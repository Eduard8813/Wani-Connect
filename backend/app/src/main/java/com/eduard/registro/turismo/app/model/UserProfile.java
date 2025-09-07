package com.eduard.registro.turismo.app.model;

import jakarta.persistence.Basic;
// Anotaciones JPA para definir la persistencia de la entidad
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// Lombok genera automáticamente getters, setters, toString, equals y hashCode
import lombok.Data;

/**
 * Entidad que representa el perfil personal de un usuario.
 * Contiene información como nombre, teléfono y dirección.
 * Se relaciona uno a uno con la entidad User.
 */
@Data // Lombok genera automáticamente los métodos necesarios para esta clase
@Entity // Marca esta clase como una entidad JPA
@Table(name = "user_profiles") // Define el nombre de la tabla en la base de datos
public class UserProfile {

    @Id // Marca este campo como clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera el ID automáticamente usando la estrategia de la base de datos
    private Long id;

    @Column(nullable = false) // El nombre es obligatorio
    private String firstName;

    // Campos opcionales del perfil
    private String lastName;
    private String phone;
    private String address;

    @Column(name = "Birth_date")
    private String birthDate;

    @Column(length = 20)
    private String gender;

    @Column(length = 100)
    private String location;

    @OneToOne // Relación uno a uno con la entidad User
    @JoinColumn(name = "user_id") // Define la columna que actúa como clave foránea
    private User user;
}

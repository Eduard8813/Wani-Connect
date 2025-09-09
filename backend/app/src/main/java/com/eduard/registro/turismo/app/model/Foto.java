package com.eduard.registro.turismo.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "fotos")
@Data
public class Foto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Campos necesarios para la imagen
    private String nombre;
    private String tipoContenido;
    
    @Lob
    private byte[] datos;
    
    // Relación uno a uno con la entidad User
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
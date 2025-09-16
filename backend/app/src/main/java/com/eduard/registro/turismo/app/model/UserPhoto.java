package com.eduard.registro.turismo.app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_photos")
@Data
public class UserPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;

    @Lob
    private byte[] data;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}

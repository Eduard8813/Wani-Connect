package com.eduard.registro.turismo.app.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String firstName;
    
    @Column(length = 50)
    private String lastName;
    
    @Column(length = 20)
    private String phone;
    
    @Column(length = 200)
    private String address;
    
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    
    @Column(length = 100s)
    private String gender;
    
    @Column(length = 100)
    private String location;
    
    @Column(name = "country_of_origin", length = 100)
    private String countryOfOrigin;
    
    @Column(length = 50)
    private String language;
    
    @Column(name = "tourist_interest", length = 200)
    private String touristInterest;
    
    @Column(length = 100)
    private String social;
    
    @Column(length = 500)
    private String description;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
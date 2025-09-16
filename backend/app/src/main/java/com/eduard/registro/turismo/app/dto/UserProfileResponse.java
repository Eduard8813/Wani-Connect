package com.eduard.registro.turismo.app.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private String gender;
    private String location;
    private String countryOfOrigin;
    private String language;
    private String touristInterest;
    private String social;
    private String description;
    private Long userId;
    private String username;
    private String email;
}
package com.eduard.registro.turismo.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.eduard.registro.turismo.app.model.SitioTuristico;
import com.eduard.registro.turismo.app.repository.SitioTuristicoRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecomendacionService {
    private final SitioTuristicoRepository sitioRepo;

    // Simulación básica de recomendación basada en el perfil/intereses
    public List<SitioTuristico> recomendarRuta(Long userId) {
        // Aquí deberías consultar el perfil/intereses del usuario
        // Como ejemplo: devolver sitios de tipo "histórico" y "museo"
        List<SitioTuristico> recomendados = new ArrayList<>();
        recomendados.addAll(sitioRepo.findByTipo("histórico"));
        recomendados.addAll(sitioRepo.findByTipo("museo"));
        return recomendados;
    }
}
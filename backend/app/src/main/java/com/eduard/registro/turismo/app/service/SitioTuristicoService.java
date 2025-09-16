package com.eduard.registro.turismo.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.eduard.registro.turismo.app.model.SitioTuristico;
import com.eduard.registro.turismo.app.repository.SitioTuristicoRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SitioTuristicoService {
    private final SitioTuristicoRepository sitioRepo;

    public SitioTuristico guardarSitio(SitioTuristico sitio) {
        sitio.setCodigoUnico("ST-" + UUID.randomUUID().toString().substring(0, 8)); // Genera código único
        return sitioRepo.save(sitio);
    }

    public SitioTuristico agregarImagenPorCodigoUnico(String codigoUnico, String base64Image) {
        SitioTuristico sitio = sitioRepo.findByCodigoUnico(codigoUnico)
            .orElseThrow(() -> new RuntimeException("Sitio no encontrado"));
        sitio.getGaleriaImagenes().add(base64Image);
        return sitioRepo.save(sitio);
    }

    public List<SitioTuristico> listarTodos() {
        return sitioRepo.findAll();
    }

    public SitioTuristico obtenerSitioPorCodigoUnico(String codigoUnico) {
        return sitioRepo.findByCodigoUnico(codigoUnico)
            .orElseThrow(() -> new RuntimeException("Sitio no encontrado"));
    }

    public List<String> listarUbicacionesSolo() {
        return sitioRepo.findAll().stream().map(SitioTuristico::getUbicacion).toList();
    }
}
package com.eduard.registro.turismo.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eduard.registro.turismo.app.dto.SitioTuristicoSinImagenDTO;
import com.eduard.registro.turismo.app.model.ImagenTuristica;
import com.eduard.registro.turismo.app.model.SitioTuristico;
import com.eduard.registro.turismo.app.repository.ImagenTuristicaRepository;
import com.eduard.registro.turismo.app.repository.SitioTuristicoRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SitioTuristicoService {

    private final SitioTuristicoRepository sitioRepo;
    private final ImagenTuristicaRepository imagenRepo;

    // 🏛️ Crear sitio turístico con código único
    public SitioTuristico guardarSitio(SitioTuristico sitio) {
        sitio.setCodigoUnico("ST-" + UUID.randomUUID().toString().substring(0, 8));
        return sitioRepo.save(sitio);
    }

    // 🖼️ Agregar imagen base64 a sitio turístico
    public SitioTuristico agregarImagenPorCodigoUnico(String codigoUnico, String base64Image) {
        SitioTuristico sitio = sitioRepo.findByCodigoUnico(codigoUnico)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sitio turístico no encontrado"));

        ImagenTuristica imagen = new ImagenTuristica();
        imagen.setGaleriaImagen(base64Image);
        imagen.setSitioTuristico(sitio);

        sitio.getGaleriaImagenes().add(imagen);
        imagenRepo.save(imagen);

        return sitioRepo.save(sitio);
    }

    // 📋 Listar todos los sitios turísticos (con imágenes incluidas)
    public List<SitioTuristico> listarTodos() {
        return sitioRepo.findAll();
    }

    // 🔍 Obtener sitio completo por código único
    public SitioTuristico obtenerSitioPorCodigoUnico(String codigoUnico) {
        return sitioRepo.findByCodigoUnico(codigoUnico)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sitio turístico no encontrado"));
    }

    // 📍 Listar solo ubicaciones únicas
    public List<String> listarUbicacionesSolo() {
        return sitioRepo.findAll().stream()
            .map(SitioTuristico::getUbicacion)
            .distinct()
            .toList();
    }

    // 🧾 Obtener sitio turístico sin imágenes (DTO)
    public SitioTuristicoSinImagenDTO obtenerSitioSinImagenPorCodigoUnico(String codigoUnico) {
        SitioTuristico sitio = sitioRepo.findByCodigoUnico(codigoUnico)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sitio turístico no encontrado"));

        SitioTuristicoSinImagenDTO dto = new SitioTuristicoSinImagenDTO();
        dto.setNombre(sitio.getNombre());
        dto.setTipo(sitio.getTipo());
        dto.setUbicacion(sitio.getUbicacion());
        dto.setHorarioAtencion(sitio.getHorarioAtencion());
        dto.setCostoEntrada(sitio.getCostoEntrada());
        dto.setCodigoUnico(sitio.getCodigoUnico());
        dto.setHistoriaResumida(sitio.getHistoriaResumida());
        dto.setEventosHistoricos(sitio.getEventosHistoricos());
        dto.setPersonajesAsociados(sitio.getPersonajesAsociados());
        dto.setAudioguiaUrl(sitio.getAudioguiaUrl());
        dto.setServiciosDisponibles(sitio.getServiciosDisponibles());
        dto.setActividadesRecomendadas(sitio.getActividadesRecomendadas());
        dto.setNivelAccesibilidad(sitio.getNivelAccesibilidad());
        dto.setReglasLugar(sitio.getReglasLugar());
        dto.setEnlaceReserva(sitio.getEnlaceReserva());

        return dto;
    }

    // 🖼️ Obtener solo las imágenes base64 de un sitio
    public List<String> obtenerImagenesBase64PorCodigoUnico(String codigoUnico) {
        SitioTuristico sitio = sitioRepo.findByCodigoUnico(codigoUnico)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sitio turístico no encontrado"));

        return sitio.getGaleriaImagenes().stream()
            .map(ImagenTuristica::getGaleriaImagen)
            .toList();
    }
}

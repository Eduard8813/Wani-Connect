package com.eduard.registro.turismo.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.eduard.registro.turismo.app.dto.SitioTuristicoSinImagenDTO;
import com.eduard.registro.turismo.app.model.ImagenTuristica;
import com.eduard.registro.turismo.app.model.SitioTuristico;
import com.eduard.registro.turismo.app.repository.SitioTuristicoRepository;
import com.eduard.registro.turismo.app.service.SitioTuristicoService;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/sitios")
@RequiredArgsConstructor
public class SitioTuristicoController {

    private final SitioTuristicoService sitioService;
    private final SitioTuristicoRepository sitioTuristicoRepository;

    // NO requiere token
    @PostMapping
    public ResponseEntity<SitioTuristico> agregarSitio(@RequestBody SitioTuristico dto) {
        return ResponseEntity.ok(sitioService.guardarSitio(dto));
    }

    // NO requiere token
    @PutMapping("/subir/{codigoUnico:.+}")
    public ResponseEntity<?> saveSitioPhotoByCodigoUnico(
        @PathVariable String codigoUnico,
        @RequestParam("file") MultipartFile file
    ) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("Debe seleccionar un archivo");
            }

            if (!file.getContentType().startsWith("image/")) {
                return ResponseEntity.badRequest().body("Solo se permiten imágenes");
            }

            SitioTuristico sitio = sitioTuristicoRepository.findByCodigoUnico(codigoUnico)
                    .orElseThrow(() -> new RuntimeException("Sitio turístico no encontrado"));

            String imageBase64 = Base64.getEncoder().encodeToString(file.getBytes());

            ImagenTuristica imagen = new ImagenTuristica();
            imagen.setGaleriaImagen(imageBase64);
            imagen.setSitioTuristico(sitio);

            sitio.getGaleriaImagenes().add(imagen);
            sitioTuristicoRepository.save(sitio);

            return ResponseEntity.ok().body("Foto del sitio turístico actualizada correctamente");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la imagen: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + e.getMessage());
        }
    }

    // SÍ requiere token — solo datos sin imágenes
    @GetMapping("/sin-imagen/{codigoUnico}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SitioTuristicoSinImagenDTO> verSitioSinImagen(@PathVariable String codigoUnico) {
        SitioTuristicoSinImagenDTO dto = sitioService.obtenerSitioSinImagenPorCodigoUnico(codigoUnico);
        return ResponseEntity.ok(dto);
    }

    // SÍ requiere token — solo imágenes del sitio
    @GetMapping("/ver-imagenes/{codigoUnico}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<String>> verImagenesPorCodigoUnico(@PathVariable String codigoUnico) {
        List<String> imagenes = sitioService.obtenerImagenesBase64PorCodigoUnico(codigoUnico);
        return ResponseEntity.ok(imagenes);
    }
}

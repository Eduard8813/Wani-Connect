package com.eduard.registro.turismo.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.eduard.registro.turismo.app.model.SitioTuristico;
import com.eduard.registro.turismo.app.service.SitioTuristicoService;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/sitios")
@RequiredArgsConstructor
public class SitioTuristicoController {
    private final SitioTuristicoService sitioService;

    // NO requiere token
    @PostMapping
    public ResponseEntity<SitioTuristico> agregarSitio(@RequestBody SitioTuristico dto) {
        return ResponseEntity.ok(sitioService.guardarSitio(dto));
    }

    // NO requiere token
    // Sube una imagen a un sitio usando el codigoUnico
    @PostMapping("/{codigoUnico}/foto")
    public ResponseEntity<SitioTuristico> subirFoto(
            @PathVariable String codigoUnico,
            @RequestParam("imagen") MultipartFile imagen
    ) throws IOException {
        // Lee el archivo y lo convierte a base64
        String base64 = "data:" + imagen.getContentType() + ";base64," +
                Base64.getEncoder().encodeToString(imagen.getBytes());

        SitioTuristico sitioActualizado = sitioService.agregarImagenPorCodigoUnico(codigoUnico, base64);
        return ResponseEntity.ok(sitioActualizado);
    }

    // SÍ requiere token
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<SitioTuristico> listarSitios() {
        return sitioService.listarTodos();
    }

    // SÍ requiere token
    @GetMapping("/{codigoUnico}")
    @PreAuthorize("isAuthenticated()")
    public SitioTuristico verSitio(@PathVariable String codigoUnico) {
        return sitioService.obtenerSitioPorCodigoUnico(codigoUnico);
    }
}
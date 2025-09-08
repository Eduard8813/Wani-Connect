package com.eduard.registro.turismo.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.eduard.registro.turismo.app.model.Foto;
import com.eduard.registro.turismo.app.model.User;
import com.eduard.registro.turismo.app.repository.FotoRepository;
import com.eduard.registro.turismo.app.security.UserDetailsImpl;

import java.util.Optional;

@RestController
@RequestMapping("/api/fotos")
public class FotoDisplayController {
    @Autowired
    private FotoRepository fotoRepository;
    
    @GetMapping("/mostrar/{id}")
    public ResponseEntity<byte[]> mostrarFoto(@PathVariable Long id) {
        Optional<Foto> fotoOptional = fotoRepository.findById(id);
        
        if (fotoOptional.isPresent()) {
            Foto foto = fotoOptional.get();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(foto.getTipoContenido()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + foto.getNombre() + "\"")
                    .body(foto.getDatos());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Endpoint para obtener la foto del usuario autenticado
    @GetMapping("/mi-foto")
    public ResponseEntity<byte[]> miFoto(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Foto foto = fotoRepository.findByUserId(user.getId());
        
        if (foto != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(foto.getTipoContenido()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + foto.getNombre() + "\"")
                    .body(foto.getDatos());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
package com.eduard.registro.turismo.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.eduard.registro.turismo.app.model.Foto;
import com.eduard.registro.turismo.app.model.User;
import com.eduard.registro.turismo.app.repository.FotoRepository;
import com.eduard.registro.turismo.app.security.UserDetailsImpl;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/fotos")
public class FotoUploadController {
    @Autowired
    private FotoRepository fotoRepository;
    
    @PostMapping("/subir")
    public ResponseEntity<?> subirFoto(
            @RequestParam("archivo") MultipartFile archivo,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        
        try {
            // Obtener el usuario autenticado desde el token
            User user = userDetails.getUser();
            
            // Verificar si el usuario ya tiene una foto
            Foto fotoExistente = fotoRepository.findByUserId(user.getId());
            
            Foto foto;
            if (fotoExistente != null) {
                // Actualizar foto existente
                foto = fotoExistente;
            } else {
                // Crear nueva foto
                foto = new Foto();
                foto.setUser(user);
            }
            
            foto.setNombre(archivo.getOriginalFilename());
            foto.setTipoContenido(archivo.getContentType());
            foto.setDatos(archivo.getBytes());
            
            fotoRepository.save(foto);
            
            return ResponseEntity.ok("Foto guardada con ID: " + foto.getId());
            
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al procesar la imagen: " + e.getMessage());
        }
    }
}
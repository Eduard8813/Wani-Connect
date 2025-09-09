package com.eduard.registro.turismo.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.eduard.registro.turismo.app.model.Foto;
import com.eduard.registro.turismo.app.model.User;
import com.eduard.registro.turismo.app.repository.FotoRepository;
import com.eduard.registro.turismo.app.repository.UserRepository;

import java.io.IOException;

@RestController
@RequestMapping("/api/fotos")
public class FotoUploadController {

    @Autowired
    private FotoRepository fotoRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/subir")
    public ResponseEntity<?> subirFoto(
            @RequestParam("archivo") MultipartFile archivo,
            Authentication authentication) {

        // Validar autenticación
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Error: No autorizado - Token no proporcionado o inválido");
        }

        try {
            // Obtener el username desde el token
            String username = authentication.getName();

            // Buscar el usuario en la base de datos
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

            // Verificar si ya tiene una foto
            Foto fotoExistente = fotoRepository.findByUserId(user.getId());

            Foto foto;
            if (fotoExistente != null) {
                foto = fotoExistente; // Actualizar
            } else {
                foto = new Foto();    // Crear nueva
                foto.setUser(user);
            }

            // Guardar datos de la imagen
            foto.setNombre(archivo.getOriginalFilename());
            foto.setTipoContenido(archivo.getContentType());
            foto.setDatos(archivo.getBytes());

            fotoRepository.save(foto);

            return ResponseEntity.ok("Foto guardada con ID: " + foto.getId());

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la imagen: " + e.getMessage());
        }
    }
}

package com.eduard.registro.turismo.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.eduard.registro.turismo.app.model.Foto;
import com.eduard.registro.turismo.app.model.User;
import com.eduard.registro.turismo.app.repository.FotoRepository;
import com.eduard.registro.turismo.app.repository.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/fotos")
public class FotoDisplayController {

    @Autowired
    private FotoRepository fotoRepository;

    @Autowired
    private UserRepository userRepository;

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

    @GetMapping("/mi-foto")
    public ResponseEntity<byte[]> miFoto(Authentication authentication) {
        // Validar autenticación
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(null);
        }

        try {
            // Obtener el username desde el token
            String username = authentication.getName();

            // Buscar el usuario en la base de datos
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

            // Buscar la foto por ID de usuario
            Foto foto = fotoRepository.findByUserId(user.getId());

            if (foto != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(foto.getTipoContenido()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + foto.getNombre() + "\"")
                        .body(foto.getDatos());
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}

package com.eduard.registro.turismo.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.eduard.registro.turismo.app.model.User;
import com.eduard.registro.turismo.app.model.UserPhoto;
import com.eduard.registro.turismo.app.repository.PhotoUserRepository;
import com.eduard.registro.turismo.app.repository.UserRepository;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/fotos")
public class FotoController {
    
    @Autowired
    private PhotoUserRepository photoUserRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/subir")
    public ResponseEntity<?> saveProfilePhoto(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User usuarioAutenticado) {
        
        try {
            // Validar que se haya enviado un archivo
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("Debe seleccionar un archivo");
            }
            
            // Validar que sea una imagen
            if (!file.getContentType().startsWith("image/")) {
                return ResponseEntity.badRequest().body("Solo se permiten imágenes");
            }
            
            // Obtener o crear la foto de perfil
            UserPhoto userPhoto = usuarioAutenticado.getPhoto();
            if (userPhoto == null) {
                userPhoto = new UserPhoto();
                userPhoto.setUser(usuarioAutenticado);
                usuarioAutenticado.setPhoto(userPhoto);
            }
            
            // Actualizar datos de la foto
            userPhoto.setFileName(file.getOriginalFilename());
            userPhoto.setFileType(file.getContentType());
            userPhoto.setData(file.getBytes());
            
            // Guardar cambios
            photoUserRepository.save(userPhoto);
            userRepository.save(usuarioAutenticado);
            
            return ResponseEntity.ok().body("Foto de perfil actualizada correctamente");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la imagen: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + e.getMessage());
        }
    }
    
    @GetMapping("/ver")
    public ResponseEntity<byte[]> getProfilePhoto(@AuthenticationPrincipal User usuarioAutenticado) {
        Optional<UserPhoto> photoOpt = photoUserRepository.findByUserId(usuarioAutenticado.getId());
        
        if (photoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        UserPhoto photo = photoOpt.get();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(photo.getFileType()))
                .body(photo.getData());
    }
    
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<byte[]> getProfilePhotoByUserId(@PathVariable Long userId) {
        return photoUserRepository.findByUserId(userId)
                .map(photo -> ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(photo.getFileType()))
                        .body(photo.getData()))
                .orElse(ResponseEntity.notFound().build());
    }
}
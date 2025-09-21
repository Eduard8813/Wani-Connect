package com.eduard.registro.turismo.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.eduard.registro.turismo.app.service.ReservaBusService;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO; 
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/qr")
public class QRValidationController {

    @Autowired
    private ReservaBusService reservaBusService;

    @PostMapping("/validar")
    public ResponseEntity<?> validarQR(@RequestParam("qrImage") MultipartFile file) {
        try {
            // Leer la imagen QR
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            
            // Decodificar el QR
            BinaryBitmap binaryBitmap = new BinaryBitmap(
                new HybridBinarizer(new BufferedImageLuminanceSource(image))
            );
            
            Result result = new MultiFormatReader().decode(binaryBitmap);
            String codigoUnico = result.getText();
            
            // Validar la reserva
            boolean validada = reservaBusService.validarReserva(codigoUnico);
            
            if (validada) {
                return ResponseEntity.ok("Reserva validada correctamente mediante QR");
            } else {
                return ResponseEntity.badRequest().body("QR inválido o reserva ya validada");
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al procesar el QR: " + e.getMessage());
        }
    }

    @GetMapping("/validar/{codigoUnico}")
    public ResponseEntity<?> validarCodigo(@PathVariable String codigoUnico) {
        try {
            boolean validada = reservaBusService.validarReserva(codigoUnico);
            
            if (validada) {
                return ResponseEntity.ok("Reserva validada correctamente");
            } else {
                return ResponseEntity.badRequest().body("Código inválido o reserva ya validada");
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al validar: " + e.getMessage());
        }
    }
}
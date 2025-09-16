package com.eduard.registro.turismo.app.controller;

import com.eduard.registro.turismo.app.dto.ComentarioRequest;
import com.eduard.registro.turismo.app.model.Comentario;
import com.eduard.registro.turismo.app.service.ComentarioService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {
    @Autowired
    private ComentarioService comentarioService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Comentario> agregarComentario(@RequestBody ComentarioRequest request) {
        return ResponseEntity.ok(comentarioService.agregarComentario(request.getCodigoUnicoConductor(), request.getComentario()));
    }

    @GetMapping("/conductor/{codigoUnico}")
    public ResponseEntity<List<Comentario>> obtenerComentariosPorConductor(@PathVariable String codigoUnico) {
        return ResponseEntity.ok(comentarioService.obtenerComentariosPorConductor(codigoUnico));
    }
}
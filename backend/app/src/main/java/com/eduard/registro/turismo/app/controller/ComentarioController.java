package com.eduard.registro.turismo.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eduard.registro.turismo.app.model.Comentario;
import com.eduard.registro.turismo.app.service.ComentarioService;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {
    @Autowired
    private ComentarioService comentarioService;
    
    @PostMapping
    public ResponseEntity<Comentario> agregarComentario(
            @RequestParam String codigoUnicoConductor,
            @RequestBody String comentario) {
        return ResponseEntity.ok(comentarioService.agregarComentario(codigoUnicoConductor, comentario));
    }
    
    @GetMapping("/conductor/{codigoUnico}")
    public ResponseEntity<List<Comentario>> obtenerComentariosPorConductor(@PathVariable String codigoUnico) {
        return ResponseEntity.ok(comentarioService.obtenerComentariosPorConductor(codigoUnico));
    }
}

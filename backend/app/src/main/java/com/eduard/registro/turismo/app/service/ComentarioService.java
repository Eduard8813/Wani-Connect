package com.eduard.registro.turismo.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduard.registro.turismo.app.model.Comentario;
import com.eduard.registro.turismo.app.model.Conductor;
import com.eduard.registro.turismo.app.repository.ComentarioRepository;
import com.eduard.registro.turismo.app.repository.ConductorRepository;

@Service
public class ComentarioService {
    @Autowired
    private ComentarioRepository comentarioRepository;
    
    @Autowired
    private ConductorRepository conductorRepository;
    
    public Comentario agregarComentario(String codigoUnicoConductor, String comentario) {
        Conductor conductor = conductorRepository.findByCodigoUnico(codigoUnicoConductor);
        if (conductor == null) {
            throw new RuntimeException("Conductor no encontrado");
        }
        
        Comentario nuevoComentario = new Comentario();
        nuevoComentario.setComentario(comentario);
        nuevoComentario.setFecha(LocalDateTime.now());
        nuevoComentario.setConductor(conductor);
        
        return comentarioRepository.save(nuevoComentario);
    }
    
    public List<Comentario> obtenerComentariosPorConductor(String codigoUnicoConductor) {
        return comentarioRepository.findByConductorCodigoUnico(codigoUnicoConductor);
    }
}

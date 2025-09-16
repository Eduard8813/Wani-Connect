package com.eduard.registro.turismo.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eduard.registro.turismo.app.model.Conductor;
import com.eduard.registro.turismo.app.repository.ConductorRepository;
import com.eduard.registro.turismo.app.repository.VehiculoRepository;

@Service
public class ConductorService {
    @Autowired
    private ConductorRepository conductorRepository;
    
    @Autowired
    private VehiculoRepository vehiculoRepository;
    
    @Value("${admin.secret.key}")
    private String adminSecretKey;
    
    public Conductor guardarConductor(Conductor conductor) {
        // Ocultar últimos 6 dígitos del teléfono
        if (conductor.getTelefono() != null && conductor.getTelefono().length() > 6) {
            String visible = conductor.getTelefono().substring(0, conductor.getTelefono().length() - 6);
            conductor.setTelefono(visible + "******");
        }
        return conductorRepository.save(conductor);
    }
    
    public Conductor obtenerConductorPorCodigo(String codigoUnico, String claveAdmin) {
        Conductor conductor = conductorRepository.findByCodigoUnico(codigoUnico);
        if (conductor == null) {
            throw new RuntimeException("Conductor no encontrado");
        }
        
        if (adminSecretKey.equals(claveAdmin)) {
            return conductor; // Devuelve todos los datos
        } else {
            // Devuelve solo datos no sensibles
            Conductor seguro = new Conductor();
            seguro.setCodigoUnico(conductor.getCodigoUnico());
            seguro.setNombre(conductor.getNombre());
            seguro.setApellido(conductor.getApellido());
            return seguro;
        }
    }
    
    public List<Conductor> obtenerConductoresDisponibles() {
        return conductorRepository.findByVehiculoAsignadoIsNull();
    }
    
    public Conductor actualizarConductor(String codigoUnico, Conductor conductorActualizado) {
        Conductor conductor = conductorRepository.findByCodigoUnico(codigoUnico);
        if (conductor == null) {
            throw new RuntimeException("Conductor no encontrado");
        }
        
        // Actualizar campos permitidos (no el código único)
        conductor.setNombre(conductorActualizado.getNombre());
        conductor.setApellido(conductorActualizado.getApellido());
        conductor.setEmail(conductorActualizado.getEmail());
        
        // Ocultar teléfono si se actualiza
        if (conductorActualizado.getTelefono() != null) {
            String telefono = conductorActualizado.getTelefono();
            if (telefono.length() > 6) {
                String visible = telefono.substring(0, telefono.length() - 6);
                conductor.setTelefono(visible + "******");
            }
        }
        
        return conductorRepository.save(conductor);
    }
}

package com.eduard.registro.turismo.app.repository;

import com.eduard.registro.turismo.app.model.BusDisponible;
import com.eduard.registro.turismo.app.model.TerminalBus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusDisponibleRepository extends JpaRepository<BusDisponible, Long> {
    
    // Encontrar todos los buses disponibles en una terminal específica
    List<BusDisponible> findByTerminalBus(TerminalBus terminalBus);
    
    // Encontrar buses disponibles por ID de terminal
    @Query("SELECT b FROM BusDisponible b WHERE b.terminalBus.id = :terminalId")
    List<BusDisponible> findByTerminalId(@Param("terminalId") Long terminalId);
    
    // Encontrar buses disponibles por terminal y con lugares disponibles
    @Query("SELECT b FROM BusDisponible b WHERE b.terminalBus.id = :terminalId AND b.lugaresDisponibles > 0")
    List<BusDisponible> findAvailableByTerminalId(@Param("terminalId") Long terminalId);
    
    // Encontrar un bus por su ID y verificar que pertenezca a una terminal específica
    @Query("SELECT b FROM BusDisponible b WHERE b.id = :busId AND b.terminalBus.id = :terminalId")
    BusDisponible findByIdAndTerminalId(@Param("busId") Long busId, @Param("terminalId") Long terminalId);
}
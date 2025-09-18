package com.eduard.registro.turismo.app.repository;

import com.eduard.registro.turismo.app.model.LugarReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LugarReservaRepository extends JpaRepository<LugarReserva, Long> {
    List<LugarReserva> findByTerminalId(Long terminalId);
    List<LugarReserva> findByTerminalIdAndDisponible(Long terminalId, boolean disponible);
}
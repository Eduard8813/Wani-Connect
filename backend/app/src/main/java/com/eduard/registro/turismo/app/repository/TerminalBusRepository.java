package com.eduard.registro.turismo.app.repository;

import com.eduard.registro.turismo.app.model.TerminalBus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TerminalBusRepository extends JpaRepository<TerminalBus, Long> {
    List<TerminalBus> findByLocalidad(String localidad);
    TerminalBus findByCodigoUnico(String codigoUnico);
}
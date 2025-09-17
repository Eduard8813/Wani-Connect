package com.eduard.registro.turismo.app.dto;

import com.eduard.registro.turismo.app.model.TerminalBus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TerminalBusDTO {
    
    @Valid
    private TerminalBus terminalBus;
    
    @NotBlank(message = "Debe proporcionar al menos una imagen")
    private String urlImagen;
}

package com.eduard.registro.turismo.app.dto;

public class ComentarioRequest {
    private String codigoUnicoConductor;
    private String comentario;

    // getters y setters
    public String getCodigoUnicoConductor() { return codigoUnicoConductor; }
    public void setCodigoUnicoConductor(String codigoUnicoConductor) { this.codigoUnicoConductor = codigoUnicoConductor; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
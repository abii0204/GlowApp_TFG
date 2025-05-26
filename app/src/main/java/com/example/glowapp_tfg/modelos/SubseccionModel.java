package com.example.glowapp_tfg.modelos;

public class SubseccionModel {
    private String subtitulo;
    private String contenido;

    public SubseccionModel() {
    }

    public SubseccionModel(String subtitulo, String contenido) {
        this.subtitulo = subtitulo;
        this.contenido = contenido;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}

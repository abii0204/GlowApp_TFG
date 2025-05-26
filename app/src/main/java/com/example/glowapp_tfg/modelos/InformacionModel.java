package com.example.glowapp_tfg.modelos;

import java.util.ArrayList;

public class InformacionModel {
    private String titulo;
    private ArrayList<SubseccionModel> subsecciones;

    public InformacionModel() {
    }

    public InformacionModel(String titulo, ArrayList<SubseccionModel> subsecciones) {
        this.titulo = titulo;
        this.subsecciones = subsecciones;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public ArrayList<SubseccionModel> getSubsecciones() {
        return subsecciones;
    }

    public void setSubsecciones(ArrayList<SubseccionModel> subsecciones) {
        this.subsecciones = subsecciones;
    }
}

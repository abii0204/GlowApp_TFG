package com.example.glowapp_tfg.modelos;

public class ResenaModel {

    private int idUsuario;
    String nombreUsuario;
    private String producto;
    private String marca;
    private String tipoPiel;
    private String comentario;
    private int puntuacion;
    private String fecha;

    public ResenaModel() {
    }

    public ResenaModel(int idUsuario, String nombreUsuario, String producto, String marca, String tipoPiel, String comentario, int puntuacion, String fecha) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.producto = producto;
        this.marca = marca;
        this.tipoPiel = tipoPiel;
        this.comentario = comentario;
        this.puntuacion = puntuacion;
        this.fecha = fecha;
    }

    public ResenaModel(int idUsuario, String producto, String marca, String tipoPiel, String comentario, int puntuacion) {
        this.idUsuario = idUsuario;
        this.producto = producto;
        this.marca = marca;
        this.tipoPiel = tipoPiel;
        this.comentario = comentario;
        this.puntuacion = puntuacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipoPiel() {
        return tipoPiel;
    }

    public void setTipoPiel(String tipoPiel) {
        this.tipoPiel = tipoPiel;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}

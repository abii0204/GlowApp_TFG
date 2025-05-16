package com.example.glowapp_tfg.modelos;

public class UsuarioModel {

    private int id;
    String nombre;
    String correo;
    String contrasena;
    String tipo_piel;

    public UsuarioModel() {
    }

    public UsuarioModel(int id, String nombre, String email, String contrasena, String tipoPiel) {
        this.id = id;
        this.nombre = nombre;
        this.correo = email;
        this.contrasena = contrasena;
        this.tipo_piel = tipoPiel;
    }

    public UsuarioModel(String nombre, String email, String contrasena, String tipoPiel) {
        this.nombre = nombre;
        this.correo = email;
        this.contrasena = contrasena;
        this.tipo_piel = tipoPiel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTipo_piel() {
        return tipo_piel;
    }

    public void setTipo_piel(String tipo_piel) {
        this.tipo_piel = tipo_piel;
    }
}

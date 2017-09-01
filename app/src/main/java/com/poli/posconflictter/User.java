package com.poli.posconflictter;

public class User {
    private String Usuario;
    private String Nombre;
    private String Apellido;
    private String Correo;

    public User () {

    }
    public User (String userid, String name, String lastname, String email) {
        this.Usuario = userid;
        this.Nombre = name;
        this.Apellido = lastname;

        this.Correo = email;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }
}
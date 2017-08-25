package com.poli.posconflictter;

public class User {
    private String Usuario;
    private String Nombre;
    private String Apellido;
    private String Correo;
    private String Contrasena;

    public User () {

    }
    public User (String userid, String name, String lastname, String email, String pass) {
        this.Usuario = userid;
        this.Nombre = name;
        this.Apellido = lastname;

        this.Correo = email;
        this.Contrasena = pass;
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

    public String getContrasena() {
        return Contrasena;
    }

    public void setContrasena(String contrasena) {
        Contrasena = contrasena;
    }
}
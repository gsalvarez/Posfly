package com.poli.posfly.museo;

public class Museum  {

    private String idMuseo;
    private String nombre;
    private String fecha;
    private String descripcion;
    private String usuario;
    private String anonimo;

    public Museum() {
        // Required empty public constructor
    }

    public Museum(String idMuseo, String name, String date, String description, String usuario, String anonimo){
        this.idMuseo=idMuseo;
        this.nombre=name;
        this.fecha=date;
        this.descripcion=description;
        this.usuario = usuario;
        this.anonimo=anonimo;

    }

    public String getIdMuseo() {return idMuseo;}
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String name){
        this.nombre=name;
    }
    public String getFecha(){
        return fecha;
    }
    public void setFecha(String date){
        this.fecha=date;
    }
    public String getDescripcion(){
        return descripcion;
    }
    public void setDescripcion(String description){
        this.descripcion=description;
    }
    public String getUsuario(){
        return usuario;
    }
    public void setUsuario(String usuario){
        this.usuario=usuario;
    }
    public String getAnonimo(){
        return anonimo;
    }
    public void setAnonimo(String anonimo){
        this.anonimo=anonimo;
    }



}
package com.poli.posfly.evento;

import java.util.ArrayList;

public class Event {

    private String nombre;
    private String fecha;
    private String hora;
    private String lugar;
    private String descripcion;
    private String precio;
    private String calificacion;
    private String id_usuario;


    public Event () {

    }

    public Event (String name, String date, String hour, String place, String description, String price, String rating, String creador) {
        this.nombre = name;
        this.fecha = date;
        this.hora = hour;
        this.lugar = place;
        this.descripcion = description;
        this.precio = price;
        this.calificacion = rating;
        this.id_usuario = creador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }
}
package com.poli.posconflictter;

import java.util.ArrayList;

public class Event {

    private String Key;
    private String Nombre;
    private String Fecha;
    private String Hora;
    private String Lugar;
    private String Descripcion;
    private String Precio;
    private double Calificacion;
    private ArrayList<String> Comentarios;
    private String Creador;


    public Event () {

    }
    public Event (String key, String name, String date, String hour, String place, String description, String price, double rating, ArrayList<String> comments, String creador) {
        this.Key = key;
        this.Nombre = name;
        this.Fecha = date;
        this.Hora = hour;
        this.Lugar = place;
        this.Descripcion = description;
        this.Precio = price;
        this.Comentarios = comments;
        this.Calificacion = rating;
        this.Creador = creador;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getLugar() {
        return Lugar;
    }

    public void setLugar(String lugar) {
        Lugar = lugar;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public double getCalificacion() {
        return Calificacion;
    }

    public void setCalificacion(double calificacion) {
        Calificacion = calificacion;
    }

    public ArrayList<String> getComentarios() {
        return Comentarios;
    }

    public void setComentarios(ArrayList<String> comentarios) {
        Comentarios = comentarios;
    }

    public void setCreador(String creador) {
        Creador = creador;
    }

    public String getCreador() {
        return Creador;
    }
}
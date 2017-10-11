package com.poli.posconflictter;

import android.*;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

public class Museum  {

    private String nombre;
    private String date;
    private String descripcion;
    private String autor;

    public Museum() {
        // Required empty public constructor
    }

    public Museum(String name, String date, String description, String author){
        this.nombre=name;
        this.date=date;
        this.descripcion=description;
        this.autor=author;

    }

    public String getNombre(){
        return nombre;
    }
    public void setNombre(String name){
        this.nombre=name;
    }
    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date=date;
    }
    public String getDescripcion(){
        return descripcion;
    }
    public void setDescripcion(String description){
        this.descripcion=description;
    }
    public String getAutor(){
        return autor;
    }
    public void setAutor(String author){
        this.autor=author;
    }



}
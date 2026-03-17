package com.pipe.avi.model;

import com.google.gson.annotations.SerializedName;

public class Programa {

    @SerializedName("id")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("nivel")
    private String nivel;

    @SerializedName("descripcion")
    private String descripcion;

    // CAMPO DE LA URL DEL AR
    @SerializedName("AR")
    private String ar;

    public Programa() {
    }

    public Programa(int id, String nombre, String nivel, String descripcion, String ar) {
        this.id = id;
        this.nombre = nombre;
        this.nivel = nivel;
        this.descripcion = descripcion;
        this.ar = ar;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNivel() {
        return nivel;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getAr() {
        return ar;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setAr(String ar) {
        this.ar = ar;
    }
}
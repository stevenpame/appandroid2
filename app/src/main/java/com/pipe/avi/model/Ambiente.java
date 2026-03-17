package com.pipe.avi.model;

import com.google.gson.annotations.SerializedName;

public class Ambiente {
    @SerializedName("id")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("coordenada_x")
    private float x;

    @SerializedName("coordenada_y")
    private float y;

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public float getX() { return x; }
    public float getY() { return y; }
}

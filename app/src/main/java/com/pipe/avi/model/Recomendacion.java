package com.pipe.avi.model;

import java.io.Serializable;

public class Recomendacion implements Serializable {

    private int idRECOMENDACION; // 🔑 ID de la recomendación
    private String nombre;       // Nombre del programa
    private String descripcion;  // Motivo/reason
    private Integer ranking;     // Ranking asignado (puede ser null)
    private int programaId;      // ID del programa asociado
    private int reporteId;       // ID del reporte asociado
    private String AR;           // Cualquier otro dato adicional que envíe el backend

    // Constructor vacío (necesario para Retrofit/Serialization)
    public Recomendacion() {}

    // Getters y Setters
    public int getIdRECOMENDACION() {
        return idRECOMENDACION;
    }

    public void setIdRECOMENDACION(int idRECOMENDACION) {
        this.idRECOMENDACION = idRECOMENDACION;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public int getProgramaId() {
        return programaId;
    }

    public void setProgramaId(int programaId) {
        this.programaId = programaId;
    }

    public int getReporteId() {
        return reporteId;
    }

    public void setReporteId(int reporteId) {
        this.reporteId = reporteId;
    }

    public String getAR() {
        return AR;
    }

    public void setAR(String AR) {
        this.AR = AR;
    }

    @Override
    public String toString() {
        return "Recomendacion{" +
                "idRECOMENDACION=" + idRECOMENDACION +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", ranking=" + ranking +
                ", programaId=" + programaId +
                ", reporteId=" + reporteId +
                ", AR='" + AR + '\'' +
                '}';
    }
}

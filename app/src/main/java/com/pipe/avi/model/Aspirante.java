package com.pipe.avi.model;

import com.google.gson.annotations.SerializedName;

public class Aspirante {

    @SerializedName("idASPIRANTE")
    private Integer idASPIRANTE;

    @SerializedName("nombre_completo")
    private String nombreCompleto;

    @SerializedName("fechaNacimiento")
    private String fechaNacimiento;

    @SerializedName("email")
    private String email;

    @SerializedName("telefono")
    private String telefono;

    @SerializedName("barrio")
    private String barrio;

    @SerializedName("direccion")
    private String direccion;

    @SerializedName("ocupacion")
    private String ocupacion;

    @SerializedName("institucion")
    private String institucion;

    @SerializedName("password")
    private String password;

    @SerializedName("activo")
    private Boolean activo;

    // 🔹 Constructor vacío (OBLIGATORIO para Retrofit)
    public Aspirante() {
    }

    // 🔹 Constructor EXACTAMENTE en el orden del backend
    public Aspirante(Integer idASPIRANTE,
                     String nombreCompleto,
                     String fechaNacimiento,
                     String email,
                     String telefono,
                     String barrio,
                     String direccion,
                     String ocupacion,
                     String institucion,
                     String password) {

        this.idASPIRANTE = idASPIRANTE;
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
        this.telefono = telefono;
        this.barrio = barrio;
        this.direccion = direccion;
        this.ocupacion = ocupacion;
        this.institucion = institucion;
        this.password = password;
    }

    // 🔹 Getters

    public Integer getIdASPIRANTE() { return idASPIRANTE; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getBarrio() { return barrio; }
    public String getDireccion() { return direccion; }
    public String getOcupacion() { return ocupacion; }
    public String getInstitucion() { return institucion; }
    public String getPassword() { return password; }
    public Boolean getActivo() { return activo; }

    // 🔹 Setters

    public void setIdASPIRANTE(Integer idASPIRANTE) { this.idASPIRANTE = idASPIRANTE; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setBarrio(String barrio) { this.barrio = barrio; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }
    public void setInstitucion(String institucion) { this.institucion = institucion; }
    public void setPassword(String password) { this.password = password; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}


package com.pipe.avi.model;

public class LoginResponse {

    private String mensaje;
    private String rol;
    private String token;
    private Aspirante usuario;

    public String getMensaje() {
        return mensaje;
    }

    public String getRol() {
        return rol;
    }

    public String getToken() {
        return token;
    }

    public Aspirante getUsuario() {
        return usuario;
    }

    public boolean isSuccess() {
        return token != null && !token.isEmpty() && usuario != null;
    }
}


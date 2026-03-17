package com.pipe.avi.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("id")
    private Integer id;

    @SerializedName("pass")
    private String pass;

    public LoginRequest(Integer id, String pass) {
        this.id = id;
        this.pass = pass;
    }
}


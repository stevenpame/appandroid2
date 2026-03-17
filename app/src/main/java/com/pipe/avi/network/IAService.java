package com.pipe.avi.network;

import com.pipe.avi.model.Preguntas;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IAService {
    @GET("/generarPregunta")
    Call<Preguntas> generarPregunta();
}


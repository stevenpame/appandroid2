package com.pipe.avi.network;

import com.pipe.avi.model.Ambiente;
import com.pipe.avi.model.Aspirante;
import com.pipe.avi.model.LoginResponse;
import com.pipe.avi.model.LoginRequest;
import com.pipe.avi.model.Programa;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AspirantesApi {

    @GET("aspirantes")
    Call<List<Aspirante>> getAspirantes();

    @GET("aspirante/{id}")
    Call<Aspirante> getAspiranteById(@Path("id") int id);

    @POST("registeraspirante")
    Call<Aspirante> registrarAspirante(@Body Aspirante aspirante);

    @POST("loginaspirante")
    Call<LoginResponse> loginAspirante(@Body LoginRequest loginRequest);

    @GET("programas")
    Call<List<Programa>> getProgramas();

    @GET("ambientes")
    Call<List<Ambiente>> getAmbientes();

    @PUT("perfilaspirante")
    Call<Aspirante> editarPerfil(@Body Aspirante aspirante);
}





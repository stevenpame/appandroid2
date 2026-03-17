package com.pipe.avi.network;

import com.pipe.avi.model.PerfilResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PerfilApi {

    @PUT("perfilaspirante/{id}")
    Call<PerfilResponse> editarPerfil(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body PerfilResponse perfil
    );

}
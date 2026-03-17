package com.pipe.avi.network;

import com.pipe.avi.model.ProgramasResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EstadisticasApi {

    @GET("estadisticas/programas")
    Call<ProgramasResponse> getProgramasRecomendados();

}

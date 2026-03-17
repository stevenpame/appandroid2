package com.pipe.avi.network;

import com.pipe.avi.model.AnswerRequest;
import com.pipe.avi.model.NextQuestionRequest;
import com.pipe.avi.model.QuestionResponse;
import com.pipe.avi.model.UpdateScoreResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RiasecApi {

    @POST("next-question")
    Call<QuestionResponse> getNextQuestion(@Body NextQuestionRequest request);

    @POST("answer") // 🔥 CORREGIDO
    Call<UpdateScoreResponse> updateScore(@Body AnswerRequest request);

}

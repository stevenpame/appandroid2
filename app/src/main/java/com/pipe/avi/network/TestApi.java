package com.pipe.avi.network;

import com.pipe.avi.model.NextQuestionRequest;
import com.pipe.avi.model.QuestionResponse;
import com.pipe.avi.model.ResultResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TestApi {

    @POST("test/pretest")
    Call<Map<String, Object>> pretest(@Body Map<String, Object> body);

    @POST("test/start")
    Call<Map<String, Object>> startTest(@Body Map<String, Object> body);

    @POST("test/next-question")
    Call<QuestionResponse> nextQuestion(@Body NextQuestionRequest body);

    @POST("test/answer")
    Call<Map<String, Object>> saveAnswer(@Body Map<String, Object> body);

    @POST("test/finish") // ✅ CORREGIDO
    Call<ResultResponse> finishTest(@Body Map<String, Object> body);

}
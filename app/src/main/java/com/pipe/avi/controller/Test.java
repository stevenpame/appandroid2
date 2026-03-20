package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.pipe.avi.R;
import com.pipe.avi.model.NextQuestionRequest;
import com.pipe.avi.model.QuestionResponse;
import com.pipe.avi.model.ResultResponse;
import com.pipe.avi.model.RiasecScores;
import com.pipe.avi.network.RetrofitClient;
import com.pipe.avi.network.TestApi;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Test extends AppCompatActivity {

    TextView txtPregunta, txtContador;
    ImageView imgLoader;
    ProgressBar progressTest;
    Button btn5, btn4, btn3, btn2, btn1;
    WebView webAvatar;
    private AvatarHelper avatarHelper;

    String sessionId;
    int reporteId;
    int aspiranteId;

    int preguntaId;
    int preguntaActual = 1;
    int totalPreguntas = 10;

    String categoriaActual;

    RiasecScores riasecScores = new RiasecScores();
    Animation fadeIn;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        txtPregunta = findViewById(R.id.txtPregunta);
        txtContador = findViewById(R.id.txtContador);
        imgLoader = findViewById(R.id.imgLoader);
        progressTest = findViewById(R.id.progressTest);
        webAvatar = findViewById(R.id.webAvatar);

        btn5 = findViewById(R.id.btn5);
        btn4 = findViewById(R.id.btn4);
        btn3 = findViewById(R.id.btn3);
        btn2 = findViewById(R.id.btn2);
        btn1 = findViewById(R.id.btn1);

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        sessionId = getIntent().getStringExtra("session_id");
        reporteId = getIntent().getIntExtra("reporteId", 0);
        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);

        avatarHelper = new AvatarHelper(this, webAvatar);

        iniciarRIASEC();
        cargarPregunta();

        btn5.setOnClickListener(v -> responder(5));
        btn4.setOnClickListener(v -> responder(4));
        btn3.setOnClickListener(v -> responder(3));
        btn2.setOnClickListener(v -> responder(2));
        btn1.setOnClickListener(v -> responder(1));
    }

    private void iniciarRIASEC() {
        riasecScores.setR(0);
        riasecScores.setI(0);
        riasecScores.setA(0);
        riasecScores.setS(0);
        riasecScores.setE(0);
        riasecScores.setC(0);
    }

    private void cargarPregunta() {
        TestApi api = RetrofitClient.getClient().create(TestApi.class);
        NextQuestionRequest request = new NextQuestionRequest(1, riasecScores, sessionId);

        api.nextQuestion(request).enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(Call<QuestionResponse> call, Response<QuestionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    QuestionResponse pregunta = response.body();
                    preguntaId = pregunta.getId();
                    categoriaActual = pregunta.getCategory();

                    txtPregunta.setText(pregunta.getQuestion());
                    txtContador.setText("Pregunta " + preguntaActual + " de " + totalPreguntas);
                    
                    // El avatar lee la pregunta
                    avatarHelper.speak(pregunta.getQuestion());
                    
                    mostrarPregunta();
                } else {
                    Toast.makeText(Test.this, "No se pudo cargar pregunta", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {
                Toast.makeText(Test.this, "Error cargando pregunta", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void responder(int valor) {
        ocultarPregunta();
        mostrarLoader(true);
        handler.postDelayed(() -> guardarRespuesta(valor), 150);
    }

    private void guardarRespuesta(int valor) {
        Map<String, Object> body = new HashMap<>();
        body.put("aspiranteId", aspiranteId);
        body.put("preguntaId", preguntaId);
        body.put("valor", valor);
        body.put("reporteId", reporteId);

        TestApi api = RetrofitClient.getClient().create(TestApi.class);
        api.saveAnswer(body).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                actualizarRIASEC(valor);
                preguntaActual++;

                if (preguntaActual > totalPreguntas) {
                    finalizarTest();
                } else {
                    cargarPregunta();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(Test.this, "Error guardando respuesta", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarPregunta() {
        mostrarLoader(false);
        txtPregunta.setVisibility(View.VISIBLE);
        txtContador.setVisibility(View.VISIBLE);
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.VISIBLE);
        btn4.setVisibility(View.VISIBLE);
        btn5.setVisibility(View.VISIBLE);
        txtPregunta.startAnimation(fadeIn);
    }

    private void ocultarPregunta() {
        txtPregunta.setVisibility(View.INVISIBLE);
        txtContador.setVisibility(View.INVISIBLE);
        btn1.setVisibility(View.INVISIBLE);
        btn2.setVisibility(View.INVISIBLE);
        btn3.setVisibility(View.INVISIBLE);
        btn4.setVisibility(View.INVISIBLE);
        btn5.setVisibility(View.INVISIBLE);
    }

    private void actualizarRIASEC(int valor) {
        if (categoriaActual != null) {
            riasecScores.updateScore(categoriaActual, valor);
        }
    }

    private void mostrarLoader(boolean mostrar) {
        if (mostrar) {
            imgLoader.setVisibility(View.VISIBLE);
            Glide.with(this).asGif().load(R.drawable.loader).into(imgLoader);
        } else {
            imgLoader.setVisibility(View.GONE);
        }
    }

    private void finalizarTest() {
        TestApi api = RetrofitClient.getClient().create(TestApi.class);
        Map<String, Object> body = new HashMap<>();
        body.put("reporteId", reporteId);
        body.put("riasec_scores", riasecScores);

        api.finishTest(body).enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResultResponse resultado = response.body();
                    List<ResultResponse.Recommendation> recomendaciones =
                            resultado.getResultadoIA() != null ?
                                    resultado.getResultadoIA().getRecommendations() : null;

                    Intent intent = new Intent(Test.this, Resultados.class);
                    intent.putExtra("resultadoIA", (Serializable) resultado);
                    intent.putExtra("aspiranteId", aspiranteId);
                    intent.putExtra("recomendaciones", (Serializable) recomendaciones);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                Toast.makeText(Test.this, "Error finalizando test", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (avatarHelper != null) avatarHelper.destroy();
        super.onDestroy();
    }
}

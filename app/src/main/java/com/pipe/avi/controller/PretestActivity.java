package com.pipe.avi.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;
import com.pipe.avi.network.ApiClient;
import com.pipe.avi.network.TestApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PretestActivity extends AppCompatActivity {

    private Button btnContinuarTest;
    private ProgressBar progressContinuar;

    private EditText pregunta1, pregunta2;
    private RadioGroup pregunta3, pregunta4, pregunta5;
    private WebView webAvatar;
    private AvatarHelper avatarHelper;

    private int aspiranteId;
    private String token;

    private int currentReadingIndex = -1;
    private final String[] preguntasText = {
        "Pregunta 1. ¿Qué querías ser cuando eras niño o niña y por qué?",
        "Pregunta 2. Si tuvieras que elegir una actividad para hacer 4 horas seguidas sin aburrirte, ¿cuál sería?",
        "Pregunta 3. ¿Qué tipo de problemas disfrutas resolver más?",
        "Pregunta 4. En un trabajo ideal, ¿qué valoras más?",
        "Pregunta 5. Cuando trabajas en equipo, ¿qué rol asumes naturalmente?"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretest);

        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);
        
        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
        token = prefs.getString("token", "");

        if (aspiranteId == 0) {
            Toast.makeText(this,"Error: aspiranteId no recibido",Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        btnContinuarTest = findViewById(R.id.btnContinuarTest);
        progressContinuar = findViewById(R.id.progressContinuar);

        pregunta1 = findViewById(R.id.pregunta1);
        pregunta2 = findViewById(R.id.pregunta2);
        pregunta3 = findViewById(R.id.pregunta3);
        pregunta4 = findViewById(R.id.pregunta4);
        pregunta5 = findViewById(R.id.pregunta5);
        webAvatar = findViewById(R.id.webAvatar);

        avatarHelper = new AvatarHelper(this, webAvatar);
        
        // Iniciar secuencia de lectura automática
        new android.os.Handler().postDelayed(() -> {
            avatarHelper.ejecutarAnimacion("Wave", 2000);
            avatarHelper.speak("Hola, bienvenido al pretest. Voy a leerte las preguntas para ayudarte.");
            
            // Empezar a leer la primera después de la bienvenida
            new android.os.Handler().postDelayed(() -> {
                readNextPregunta();
            }, 5000);
        }, 1500);

        setupListeners();

        findViewById(R.id.main).startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.fade_in)
        );

        btnContinuarTest.setOnClickListener(v -> {
            Animation press = AnimationUtils.loadAnimation(this, R.anim.boton_press);
            v.startAnimation(press);
            validarYContinuar();
        });
    }

    private void readNextPregunta() {
        currentReadingIndex++;
        if (currentReadingIndex < preguntasText.length) {
            avatarHelper.speak(preguntasText[currentReadingIndex]);
            // Enfocar visualmente el campo correspondiente
            focusPregunta(currentReadingIndex);
        }
    }

    private void focusPregunta(int index) {
        switch (index) {
            case 0: pregunta1.requestFocus(); break;
            case 1: pregunta2.requestFocus(); break;
            case 2: pregunta3.requestFocus(); break;
            case 3: pregunta4.requestFocus(); break;
            case 4: pregunta5.requestFocus(); break;
        }
    }

    private void setupListeners() {
        // Si el usuario toca manualmente una pregunta, actualizamos el índice para que sepa dónde está
        pregunta1.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                currentReadingIndex = 0;
                avatarHelper.speak(preguntasText[0]);
            }
        });
        pregunta2.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                currentReadingIndex = 1;
                avatarHelper.speak(preguntasText[1]);
            }
        });

        pregunta3.setOnCheckedChangeListener((group, checkedId) -> {
            currentReadingIndex = 2;
            animarSeleccion(checkedId);
            // Al responder, podemos leer la siguiente automáticamente después de un pequeño delay
            checkAutoReadNext(3);
        });

        pregunta4.setOnCheckedChangeListener((group, checkedId) -> {
            currentReadingIndex = 3;
            animarSeleccion(checkedId);
            checkAutoReadNext(4);
        });

        pregunta5.setOnCheckedChangeListener((group, checkedId) -> {
            currentReadingIndex = 4;
            animarSeleccion(checkedId);
        });
    }

    private void checkAutoReadNext(int nextIndex) {
        new android.os.Handler().postDelayed(() -> {
            if (currentReadingIndex < nextIndex) {
                readNextPregunta();
            }
        }, 2000);
    }

    private void animarSeleccion(int checkedId) {
        RadioButton seleccionado = findViewById(checkedId);
        if(seleccionado != null){
            Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
            seleccionado.startAnimation(zoom);
        }
    }

    private void validarYContinuar() {
        boolean valido = true;
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        if (pregunta1.getText().toString().trim().isEmpty()) {
            pregunta1.setError("Obligatorio");
            pregunta1.startAnimation(shake);
            valido = false;
        }

        if (pregunta2.getText().toString().trim().isEmpty()) {
            pregunta2.setError("Obligatorio");
            pregunta2.startAnimation(shake);
            valido = false;
        }

        if (pregunta3.getCheckedRadioButtonId() == -1) {
            pregunta3.startAnimation(shake);
            valido = false;
        }
        if (pregunta4.getCheckedRadioButtonId() == -1) {
            pregunta4.startAnimation(shake);
            valido = false;
        }
        if (pregunta5.getCheckedRadioButtonId() == -1) {
            pregunta5.startAnimation(shake);
            valido = false;
        }

        if (!valido) {
            avatarHelper.speak("Por favor, completa todas las respuestas para poder continuar.");
            Toast.makeText(this,"Responde todas las preguntas",Toast.LENGTH_LONG).show();
            return;
        }

        btnContinuarTest.setEnabled(false);
        progressContinuar.setVisibility(android.view.View.VISIBLE);
        crearReporte();
    }

    private void crearReporte(){
        Map<String,Object> body = new HashMap<>();
        body.put("aspiranteId", aspiranteId);
        TestApi api = ApiClient.getClient().create(TestApi.class);
        api.startTest(body).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    Number reporteNumber = (Number) response.body().get("reporteId");
                    if(reporteNumber != null){
                        enviarPretest(reporteNumber.intValue());
                    } else {
                        errorUI("Error: reporteId nulo");
                    }
                } else {
                    errorUI("Error creando reporte");
                }
            }
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                errorUI("Error de conexión");
            }
        });
    }

    private void enviarPretest(int reporteId){
        String r3 = ((RadioButton)findViewById(pregunta3.getCheckedRadioButtonId())).getText().toString();
        String r4 = ((RadioButton)findViewById(pregunta4.getCheckedRadioButtonId())).getText().toString();
        String r5 = ((RadioButton)findViewById(pregunta5.getCheckedRadioButtonId())).getText().toString();

        List<String> answers = new ArrayList<>();
        answers.add(pregunta1.getText().toString().trim());
        answers.add(pregunta2.getText().toString().trim());
        answers.add(r3);
        answers.add(r4);
        answers.add(r5);

        Map<String,Object> body = new HashMap<>();
        body.put("aspiranteId", aspiranteId);
        body.put("reporteId", reporteId);
        body.put("answers", answers);

        TestApi api = ApiClient.getClient().create(TestApi.class);
        api.pretest(body).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    String sessionId = (String) response.body().get("session_id");
                    Intent intent = new Intent(PretestActivity.this, Test.class);
                    intent.putExtra("session_id",sessionId);
                    intent.putExtra("reporteId",reporteId);
                    intent.putExtra("aspiranteId",aspiranteId);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    errorUI("Error enviando pretest");
                }
            }
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                errorUI("Error conexión pretest");
            }
        });
    }

    private void errorUI(String mensaje){
        Toast.makeText(PretestActivity.this, mensaje, Toast.LENGTH_LONG).show();
        btnContinuarTest.setEnabled(true);
        progressContinuar.setVisibility(android.view.View.GONE);
    }

    @Override
    protected void onDestroy() {
        if (avatarHelper != null) avatarHelper.destroy();
        super.onDestroy();
    }
}

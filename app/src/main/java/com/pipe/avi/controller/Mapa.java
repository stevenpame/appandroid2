package com.pipe.avi.controller;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;
import com.pipe.avi.model.Programa;
import com.pipe.avi.network.ApiClient;
import com.pipe.avi.network.AspirantesApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Mapa extends AppCompatActivity {

    private WebView mapa3dWebView;
    private LinearLayout layoutBotonesRecomendados;
    private Button btnVerMasProgramas;

    private int aspiranteId;

    // 🔥 PROGRAMAS Y IDS QUE VIENEN DESDE INFOPROGRAMAS
    private ArrayList<String> programasRecomendados;
    private ArrayList<Integer> idPROGRAMA;        // 🔑 IDs de los programas
    private ArrayList<Integer> recomendacionIds;  // IDs de la recomendación (idRECOMENDACION)

    // 🔥 PROGRAMAS DEL BACKEND
    private List<Programa> todosLosProgramas = new ArrayList<>();

    private Animation pressAnim;
    private Animation pulseAnim;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        mapa3dWebView = findViewById(R.id.mapa3dWebView);
        layoutBotonesRecomendados = findViewById(R.id.layoutBotonesRecomendados);
        btnVerMasProgramas = findViewById(R.id.btnVerMasProgramas);

        // 🔥 RECIBIR DATOS
        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);
        programasRecomendados = getIntent().getStringArrayListExtra("programas");
        idPROGRAMA = getIntent().getIntegerArrayListExtra("idPROGRAMA");
        recomendacionIds = getIntent().getIntegerArrayListExtra("recomendacionIds");

        if (aspiranteId == 0) {
            Toast.makeText(this, "Error: aspiranteId no recibido", Toast.LENGTH_LONG).show();
        }

        if (programasRecomendados == null) programasRecomendados = new ArrayList<>();
        if (idPROGRAMA == null) idPROGRAMA = new ArrayList<>();
        if (recomendacionIds == null) recomendacionIds = new ArrayList<>();

        // 🔥 ANIMACIONES
        pressAnim = AnimationUtils.loadAnimation(this, R.anim.boton_press);
        pulseAnim = AnimationUtils.loadAnimation(this, R.anim.pulse);

        configurarWebView();
        cargarProgramas();

        // 🔥 BOTÓN VER MÁS -> RankingProgramas
        btnVerMasProgramas.setOnClickListener(v -> {
            v.startAnimation(pressAnim);
            Intent intent = new Intent(Mapa.this, RankingProgramas.class);
            intent.putStringArrayListExtra("programas", programasRecomendados);
            intent.putIntegerArrayListExtra("idPROGRAMAs", idPROGRAMA); // Cambiado a idPROGRAMAs para coincidir con lo que Ranking espera
            intent.putIntegerArrayListExtra("recomendacionIds", recomendacionIds);
            intent.putExtra("aspiranteId", aspiranteId);
            intent.putExtra("reporteId", getIntent().getIntExtra("reporteId", -1)); // Intentar pasar reporteId si existe
            startActivity(intent);
        });

        // 🔥 BOTONES DE MENÚ
        findViewById(R.id.btnhome).setOnClickListener(v -> {
            v.startAnimation(pressAnim);
            finish();
        });

        findViewById(R.id.btnusuario).setOnClickListener(v -> {
            v.startAnimation(pressAnim);
            Intent intent = new Intent(Mapa.this, User.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void configurarWebView() {
        WebSettings webSettings = mapa3dWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        mapa3dWebView.setWebViewClient(new WebViewClient());
        mapa3dWebView.addJavascriptInterface(new WebAppInterface(), "Android");
        mapa3dWebView.loadUrl("file:///android_asset/viewer3d.html");
    }

    private void cargarProgramas() {
        AspirantesApi api = ApiClient.getClient().create(AspirantesApi.class);
        api.getProgramas().enqueue(new Callback<List<Programa>>() {
            @Override
            public void onResponse(Call<List<Programa>> call, Response<List<Programa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    todosLosProgramas = response.body();
                    crearBotones();
                } else {
                    Toast.makeText(Mapa.this, "No se pudieron cargar programas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Programa>> call, Throwable t) {
                Toast.makeText(Mapa.this, "Error al cargar programas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void crearBotones() {
        layoutBotonesRecomendados.removeAllViews();

        // 🔥 ASEGURAR QUE SE MUESTREN LOS 3
        int limite = Math.min(3, programasRecomendados.size());

        for (int i = 0; i < limite; i++) {
            String nombreRecomendado = programasRecomendados.get(i);
            
            // Buscar programa en la lista del backend (comparación más flexible)
            Programa programaMatch = null;
            for (Programa p : todosLosProgramas) {
                if (p.getNombre() != null && 
                    (p.getNombre().equalsIgnoreCase(nombreRecomendado) || 
                     nombreRecomendado.toLowerCase().contains(p.getNombre().toLowerCase()))) {
                    programaMatch = p;
                    break;
                }
            }

            // Si no lo encuentra en el backend, creamos un botón genérico con el nombre recibido
            Button btn = new Button(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            params.setMargins(10, 5, 10, 5);
            btn.setLayoutParams(params);

            btn.setText(nombreRecomendado);
            btn.setTextSize(8); // Reducir un poco el tamaño para que quepan 3
            btn.setAllCaps(false);
            btn.setBackgroundResource(R.drawable.bg_boton_acceso);
            btn.setTextColor(Color.BLACK);

            btn.startAnimation(pulseAnim);
            animarColor(btn);

            int finalI = i;
            Programa finalProgramaMatch = programaMatch;
            
            btn.setOnClickListener(v -> {
                v.clearAnimation();
                v.startAnimation(pressAnim);

                int programaIdSeleccionado = (finalI < idPROGRAMA.size()) ? idPROGRAMA.get(finalI) : 0;
                
                // Si encontramos el programa en el backend y tiene AR, lo lanzamos
                if (finalProgramaMatch != null && finalProgramaMatch.getAr() != null && !finalProgramaMatch.getAr().isEmpty()) {
                    Intent intent = new Intent(Mapa.this, ARActivity.class);
                    intent.putExtra("url_ar", finalProgramaMatch.getAr());
                    intent.putExtra("programa", finalProgramaMatch.getNombre());
                    intent.putExtra("idPROGRAMA", programaIdSeleccionado);
                    intent.putExtra("aspiranteId", aspiranteId);
                    v.postDelayed(() -> startActivity(intent), 200);
                } else {
                    Toast.makeText(this, "Experiencia AR no disponible para: " + nombreRecomendado, Toast.LENGTH_SHORT).show();
                    v.startAnimation(pulseAnim);
                }
            });

            layoutBotonesRecomendados.addView(btn);
        }
    }

    private void animarColor(Button btn) {
        ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), Color.WHITE, Color.parseColor("#90EE90"));
        anim.setDuration(2000);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.addUpdateListener(a -> {
            if (btn.getBackground() != null) btn.getBackground().setTint((int) a.getAnimatedValue());
        });
        anim.start();
    }

    private Programa buscarPrograma(String nombre) {
        for (Programa p : todosLosProgramas) {
            if (p.getNombre() != null && p.getNombre().equalsIgnoreCase(nombre)) return p;
        }
        return null;
    }

    public class WebAppInterface {
        @JavascriptInterface
        public void onProgramClicked(String nombre) {
            runOnUiThread(() -> {
                Programa p = buscarPrograma(nombre);
                if (p != null && p.getAr() != null && !p.getAr().isEmpty()) {
                    int index = programasRecomendados.indexOf(nombre);
                    int programaId = (index >= 0 && index < idPROGRAMA.size()) ? idPROGRAMA.get(index) : 0;

                    Intent intent = new Intent(Mapa.this, ARActivity.class);
                    intent.putExtra("url_ar", p.getAr());
                    intent.putExtra("programa", p.getNombre());
                    intent.putExtra("idPROGRAMA", programaId);
                    intent.putExtra("aspiranteId", aspiranteId);
                    startActivity(intent);
                }
            });
        }
    }
}
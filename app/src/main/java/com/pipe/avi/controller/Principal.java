package com.pipe.avi.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.pipe.avi.R;
import com.pipe.avi.model.ProgramaEstadistica;
import com.pipe.avi.model.ProgramasResponse;
import com.pipe.avi.network.EstadisticasApi;
import com.pipe.avi.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Principal extends AppCompatActivity {

    ImageButton btnusuario;
    LinearLayout lyprogramas, lyresultados, lytest;
    TextView txtAccesos;

    PieChart chartProgramas;
    LinearLayout legendContainer;

    int aspiranteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        aspiranteId = getIntent().getIntExtra("aspiranteId", -1);

        if (aspiranteId == -1) {
            Toast.makeText(this, "Error: sesión no válida", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Principal.this, IniciarSesion.class);
            startActivity(intent);
            finish();
            return;
        }

        btnusuario = findViewById(R.id.btnusuario);

        lyprogramas = findViewById(R.id.lyprogramas);
        lyresultados = findViewById(R.id.lyresultados);
        lytest = findViewById(R.id.lytest);

        txtAccesos = findViewById(R.id.txtAccesos);

        chartProgramas = findViewById(R.id.chartProgramas);
        legendContainer = findViewById(R.id.legendContainer);

        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation press = AnimationUtils.loadAnimation(this, R.anim.boton_press);

        txtAccesos.startAnimation(fade);

        lytest.postDelayed(() -> lytest.startAnimation(slide), 100);
        lyresultados.postDelayed(() -> lyresultados.startAnimation(slide), 200);
        lyprogramas.postDelayed(() -> lyprogramas.startAnimation(slide), 300);

        cargarGraficoProgramas();

        btnusuario.setOnClickListener(v -> {
            v.startAnimation(press);
            Intent intent = new Intent(Principal.this, User.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });


        lyprogramas.setOnClickListener(v -> {
            v.startAnimation(press);
            Intent intent = new Intent(Principal.this, Programas.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        lyresultados.setOnClickListener(v -> {
            v.startAnimation(press);
            Intent intent = new Intent(Principal.this, Resultados.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        lytest.setOnClickListener(v -> {
            v.startAnimation(press);
            Intent intent = new Intent(Principal.this, BienvenidaTest.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void cargarGraficoProgramas() {

        EstadisticasApi api = RetrofitClient.getClient().create(EstadisticasApi.class);

        api.getProgramasRecomendados().enqueue(new Callback<ProgramasResponse>() {

            @Override
            public void onResponse(Call<ProgramasResponse> call, Response<ProgramasResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<PieEntry> entries = new ArrayList<>();

                    int[] colores = {
                            Color.parseColor("#73C6D9"),
                            Color.parseColor("#6EF05C"),
                            Color.parseColor("#C65AB4"),
                            Color.parseColor("#4DB6AC"),
                            Color.parseColor("#8E6CE5")
                    };

                    int index = 0;

                    legendContainer.removeAllViews();

                    for (ProgramaEstadistica p : response.body().getData()) {

                        entries.add(new PieEntry(p.getTotal(), p.getPrograma()));

                        LinearLayout fila = new LinearLayout(Principal.this);
                        fila.setOrientation(LinearLayout.HORIZONTAL);
                        fila.setPadding(0,10,0,10);

                        TextView colorBox = new TextView(Principal.this);
                        colorBox.setBackgroundColor(colores[index]);
                        colorBox.setWidth(30);
                        colorBox.setHeight(30);

                        TextView texto = new TextView(Principal.this);
                        texto.setText("  " + p.getPrograma() + " (" + p.getTotal() + ")");
                        texto.setTextSize(13f);

                        fila.addView(colorBox);
                        fila.addView(texto);

                        legendContainer.addView(fila);

                        index++;
                    }

                    PieDataSet dataSet = new PieDataSet(entries, "");

                    dataSet.setColors(colores);

                    dataSet.setValueTextSize(14f);
                    dataSet.setValueTextColor(Color.WHITE);

                    PieData data = new PieData(dataSet);

                    chartProgramas.setData(data);

                    chartProgramas.setEntryLabelColor(Color.TRANSPARENT);
                    chartProgramas.setEntryLabelTextSize(0f);

                    chartProgramas.getLegend().setEnabled(false);

                    chartProgramas.setDrawHoleEnabled(true);
                    chartProgramas.setHoleRadius(60f);
                    chartProgramas.setTransparentCircleRadius(65f);

                    chartProgramas.getDescription().setEnabled(false);

                    chartProgramas.animateY(1400);
                    chartProgramas.invalidate();
                }
            }

            @Override
            public void onFailure(Call<ProgramasResponse> call, Throwable t) {

                Toast.makeText(Principal.this,
                        "Error cargando estadísticas",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
}
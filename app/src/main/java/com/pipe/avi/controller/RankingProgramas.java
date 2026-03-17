package com.pipe.avi.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RankingProgramas extends AppCompatActivity {

    private Spinner spinner1, spinner2, spinner3;
    private Button btnGuardar;

    private ArrayList<String> programasOriginal;
    private ArrayList<Integer> recomendacionIds;
    private ArrayList<Integer> idPROGRAMAs; // ⚡ Cambiado a idPROGRAMA
    private int aspiranteId;
    private int reporteId;

    private ArrayAdapter<String> adapter1, adapter2, adapter3;
    private boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_programas);

        spinner1 = findViewById(R.id.spinnerPrimero);
        spinner2 = findViewById(R.id.spinnerSegundo);
        spinner3 = findViewById(R.id.spinnerTercero);
        btnGuardar = findViewById(R.id.btnGuardarRanking);

        programasOriginal = getIntent().getStringArrayListExtra("programas");
        recomendacionIds = getIntent().getIntegerArrayListExtra("recomendacionIds");
        idPROGRAMAs = getIntent().getIntegerArrayListExtra("idPROGRAMAs"); // ⚡ recibe idPROGRAMA
        aspiranteId = getIntent().getIntExtra("aspiranteId", -1);
        reporteId = getIntent().getIntExtra("reporteId", -1);

        Log.d("DEBUG", "AspiranteID: " + aspiranteId);
        Log.d("DEBUG", "recomendacionIds: " + recomendacionIds);
        Log.d("DEBUG", "idPROGRAMAs: " + idPROGRAMAs);
        Log.d("DEBUG", "reporteId: " + reporteId);

        if (aspiranteId == -1 || recomendacionIds == null || recomendacionIds.size() < 3
                || idPROGRAMAs == null || idPROGRAMAs.size() < 3 || reporteId == -1) {
            Toast.makeText(this, "Error: Datos incompletos", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (programasOriginal == null) programasOriginal = new ArrayList<>();
        if (!programasOriginal.contains("Selecciona un programa")) programasOriginal.add(0, "Selecciona un programa");

        configurarSpinners();
        btnGuardar.setOnClickListener(v -> guardarRanking());
    }

    private void configurarSpinners() {
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(programasOriginal));
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(programasOriginal));
        adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(programasOriginal));

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isUpdating) return;

                String seleccionado = parent.getItemAtPosition(position).toString();
                String primero = getSeleccion(spinner1);
                String segundo = getSeleccion(spinner2);
                String tercero = getSeleccion(spinner3);

                isUpdating = true;

                if (parent == spinner1 && (seleccionado.equals(segundo) || seleccionado.equals(tercero)))
                    spinner1.setSelection(0, false);
                else if (parent == spinner2 && (seleccionado.equals(primero) || seleccionado.equals(tercero)))
                    spinner2.setSelection(0, false);
                else if (parent == spinner3 && (seleccionado.equals(primero) || seleccionado.equals(segundo)))
                    spinner3.setSelection(0, false);

                actualizarSpinners();
                isUpdating = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinner1.setOnItemSelectedListener(listener);
        spinner2.setOnItemSelectedListener(listener);
        spinner3.setOnItemSelectedListener(listener);
    }

    private void actualizarSpinners() {
        if (isUpdating) return;
        isUpdating = true;

        String primero = getSeleccion(spinner1);
        String segundo = getSeleccion(spinner2);
        String tercero = getSeleccion(spinner3);

        actualizarLista(adapter1, programasOriginal, segundo, tercero, primero, spinner1);
        actualizarLista(adapter2, programasOriginal, primero, tercero, segundo, spinner2);
        actualizarLista(adapter3, programasOriginal, primero, segundo, tercero, spinner3);

        isUpdating = false;
    }

    private String getSeleccion(Spinner spinner) {
        return spinner.getSelectedItem() != null ? spinner.getSelectedItem().toString() : null;
    }

    private void actualizarLista(ArrayAdapter<String> adapter,
                                 ArrayList<String> base,
                                 String excluir1,
                                 String excluir2,
                                 String seleccionActual,
                                 Spinner spinner) {

        adapter.clear();
        for (String item : base) {
            if (item.equals("Selecciona un programa")) {
                adapter.add(item);
                continue;
            }
            if ((excluir1 != null && item.equals(excluir1)) || (excluir2 != null && item.equals(excluir2))) continue;
            adapter.add(item);
        }

        adapter.notifyDataSetChanged();
        if (seleccionActual != null) {
            int pos = adapter.getPosition(seleccionActual);
            if (pos >= 0) spinner.setSelection(pos, false);
        }
    }

    private void guardarRanking() {

        String primero = getSeleccion(spinner1);
        String segundo = getSeleccion(spinner2);
        String tercero = getSeleccion(spinner3);

        if (primero.equals("Selecciona un programa") ||
                segundo.equals("Selecciona un programa") ||
                tercero.equals("Selecciona un programa") ||
                primero.equals(segundo) || primero.equals(tercero) || segundo.equals(tercero)) {

            Toast.makeText(this, "Selecciona 3 programas distintos", Toast.LENGTH_LONG).show();
            return;
        }

        new Thread(() -> {
            try {
                SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
                String token = prefs.getString("token", null);

                if (token == null || token.isEmpty()) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Error: Token no disponible", Toast.LENGTH_LONG).show());
                    return;
                }

                URL url = new URL("https://avibackcopia2-production.up.railway.app/api/test/ranking");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Authorization", "Bearer " + token);
                conn.setDoOutput(true);

                JSONArray rankings = new JSONArray();

                JSONObject r1 = new JSONObject();
                r1.put("nombre", primero);
                r1.put("ranking", 3);
                r1.put("idRECOMENDACION", recomendacionIds.get(0));
                r1.put("idPROGRAMA", idPROGRAMAs.get(0));
                r1.put("reporteId", reporteId);

                JSONObject r2 = new JSONObject();
                r2.put("nombre", segundo);
                r2.put("ranking", 2);
                r2.put("idRECOMENDACION", recomendacionIds.get(1));
                r2.put("idPROGRAMA", idPROGRAMAs.get(1));
                r2.put("reporteId", reporteId);

                JSONObject r3 = new JSONObject();
                r3.put("nombre", tercero);
                r3.put("ranking", 1);
                r3.put("idRECOMENDACION", recomendacionIds.get(2));
                r3.put("idPROGRAMA", idPROGRAMAs.get(2));
                r3.put("reporteId", reporteId);

                rankings.put(r1);
                rankings.put(r2);
                rankings.put(r3);

                JSONObject json = new JSONObject();
                json.put("aspiranteId", aspiranteId);
                json.put("rankings", rankings);

                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                int response = conn.getResponseCode();
                conn.disconnect();

                runOnUiThread(() -> {
                    if (response == 200 || response == 201) {
                        Toast.makeText(this, "Ranking guardado correctamente", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Error guardando ranking: " + response, Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                Log.e("ERROR", e.toString());
                runOnUiThread(() ->
                        Toast.makeText(this, "Error de conexión: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}
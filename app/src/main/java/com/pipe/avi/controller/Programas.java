package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pipe.avi.R;
import com.pipe.avi.model.Programa;
import com.pipe.avi.network.ApiClient;
import com.pipe.avi.network.AspirantesApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Programas extends AppCompatActivity {

    private ImageButton btnhome, btnusuario;
    private RecyclerView rvProgramas;
    private ProgramaAdapter adapter;

    private List<Programa> listaProgramas = new ArrayList<>();
    private List<Programa> listaFiltrada = new ArrayList<>();

    private EditText sbprogramas;
    private Spinner spinnerNivel;
    private ProgressBar progressBar;

    private LinearLayout headerLayout;
    private ImageView logoAvi;

    Animation press;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programas);

        btnhome = findViewById(R.id.btnhome);
        btnusuario = findViewById(R.id.btnusuario);

        rvProgramas = findViewById(R.id.rvProgramas);
        sbprogramas = findViewById(R.id.sbprogramas);
        spinnerNivel = findViewById(R.id.spinnerNivel);
        progressBar = findViewById(R.id.progressBar);

        headerLayout = findViewById(R.id.headerLayout);
        logoAvi = findViewById(R.id.logoAvi);

        rvProgramas.setLayoutManager(new LinearLayoutManager(this));

        // 🔹 Animación del RecyclerView
        LayoutAnimationController animation =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_recycler);
        rvProgramas.setLayoutAnimation(animation);

        // 🔹 Cargar animaciones
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        press = AnimationUtils.loadAnimation(this, R.anim.boton_press);

        // 🔹 Animaciones iniciales
        headerLayout.startAnimation(fade);
        logoAvi.startAnimation(zoom);
        rvProgramas.startAnimation(slide);

        // 🔹 Adapter
        adapter = new ProgramaAdapter(new ArrayList<>(), programa -> {

            String urlAR = programa.getAr();

            if (urlAR == null || urlAR.isEmpty()) {
                Toast.makeText(Programas.this,
                        "Este programa no tiene experiencia AR.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(Programas.this, ARActivity.class);
            intent.putExtra("url_ar", urlAR);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        rvProgramas.setAdapter(adapter);

        setupSpinner();
        setupSearch();
        cargarProgramas();

        // 🔹 BOTÓN HOME
        btnhome.setOnClickListener(v -> {

            v.startAnimation(press);
            finish();
        });

        // 🔹 BOTÓN USUARIO
        btnusuario.setOnClickListener(v -> {

            v.startAnimation(press);

            startActivity(new Intent(Programas.this, User.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

    }

    private void cargarProgramas() {

        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);

        AspirantesApi api = ApiClient.getClient().create(AspirantesApi.class);

        api.getProgramas().enqueue(new Callback<List<Programa>>() {

            @Override
            public void onResponse(Call<List<Programa>> call, Response<List<Programa>> response) {

                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {

                    listaProgramas = response.body();
                    filtrar();

                } else {

                    Toast.makeText(Programas.this,
                            "Error del servidor: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Programa>> call, Throwable t) {

                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);

                Toast.makeText(Programas.this,
                        "Fallo de conexión: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinner() {

        String[] niveles = {"Todos", "Técnico", "Tecnólogo"};

        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item,
                        niveles);

        spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinnerNivel.setAdapter(spinnerAdapter);

        spinnerNivel.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view,
                                               int position,
                                               long id) {
                        filtrar();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
    }

    private void setupSearch() {

        sbprogramas.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {}

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {
                filtrar();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filtrar() {

        String query =
                sbprogramas.getText().toString().toLowerCase();

        String nivelSeleccionado =
                spinnerNivel.getSelectedItem().toString();

        listaFiltrada.clear();

        for (Programa p : listaProgramas) {

            String nombre =
                    p.getNombre() != null ?
                            p.getNombre().toLowerCase() : "";

            String nivel =
                    p.getNivel() != null ?
                            p.getNivel() : "";

            boolean coincideBusqueda =
                    nombre.contains(query);

            boolean coincideNivel =
                    nivelSeleccionado.equals("Todos") ||
                            nivel.equalsIgnoreCase(nivelSeleccionado);

            if (coincideBusqueda && coincideNivel) {
                listaFiltrada.add(p);
            }
        }

        adapter.updateList(new ArrayList<>(listaFiltrada));

        // 🔥 Ejecutar animación de lista
        rvProgramas.scheduleLayoutAnimation();
    }
}
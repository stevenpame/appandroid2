package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

import java.util.ArrayList;

public class InfoProgramas extends AppCompatActivity {

    TextView txtPrograma1, txtPrograma2, txtPrograma3;
    TextView txtNivel1, txtNivel2, txtNivel3;
    TextView txtDuracion1, txtDuracion2, txtDuracion3;
    TextView txtDescripcion1, txtDescripcion2, txtDescripcion3;

    Button btnIrMapa;
    WebView webAvatar;
    private AvatarHelper avatarHelper;

    ArrayList<String> programas;
    ArrayList<Integer> programIds;
    ArrayList<Integer> recomendacionIds;

    int aspiranteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_programas);

        txtPrograma1 = findViewById(R.id.txtPrograma1);
        txtPrograma2 = findViewById(R.id.txtPrograma2);
        txtPrograma3 = findViewById(R.id.txtPrograma3);
        txtNivel1 = findViewById(R.id.txtNivel1);
        txtNivel2 = findViewById(R.id.txtNivel2);
        txtNivel3 = findViewById(R.id.txtNivel3);
        txtDuracion1 = findViewById(R.id.txtDuracion1);
        txtDuracion2 = findViewById(R.id.txtDuracion2);
        txtDuracion3 = findViewById(R.id.txtDuracion3);
        txtDescripcion1 = findViewById(R.id.txtDescripcion1);
        txtDescripcion2 = findViewById(R.id.txtDescripcion2);
        txtDescripcion3 = findViewById(R.id.txtDescripcion3);
        btnIrMapa = findViewById(R.id.btnIrMapa);
        webAvatar = findViewById(R.id.webAvatar);

        avatarHelper = new AvatarHelper(this, webAvatar);
        
        programas = getIntent().getStringArrayListExtra("programas");
        programIds = getIntent().getIntegerArrayListExtra("programIds");
        recomendacionIds = getIntent().getIntegerArrayListExtra("recomendacionIds");
        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);

        if (programas != null && !programas.isEmpty()) {
            if (programas.size() > 0) llenarPrograma(programas.get(0), txtPrograma1, txtNivel1, txtDuracion1, txtDescripcion1);
            if (programas.size() > 1) llenarPrograma(programas.get(1), txtPrograma2, txtNivel2, txtDuracion2, txtDescripcion2);
            if (programas.size() > 2) llenarPrograma(programas.get(2), txtPrograma3, txtNivel3, txtDuracion3, txtDescripcion3);
            
            // Avatar "explica" los programas al entrar
            avatarHelper.animarHablar(5000);
        }

        btnIrMapa.setOnClickListener(v -> {
            Intent intent = new Intent(InfoProgramas.this, Mapa.class);
            intent.putStringArrayListExtra("programas", programas);
            intent.putIntegerArrayListExtra("idPROGRAMA", programIds);
            intent.putIntegerArrayListExtra("recomendacionIds", recomendacionIds);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });
    }

    private void llenarPrograma(String nombre, TextView txtNombre, TextView txtNivel, TextView txtDuracion, TextView txtDescripcion) {
        txtNombre.setText(nombre);
        String nivel, duracion;
        if (nombre.toLowerCase().contains("tecnico") || nombre.toLowerCase().contains("técnico")) {
            nivel = "Nivel: Técnico";
            duracion = "Duración: 2 años";
        } else {
            nivel = "Nivel: Tecnólogo";
            duracion = "Duración: 3 años";
        }
        txtNivel.setText(nivel);
        txtDuracion.setText(duracion);
        txtDescripcion.setText("Este programa de formación en " + nombre + " permite desarrollar habilidades técnicas y prácticas...");
    }

    @Override
    protected void onDestroy() {
        if (avatarHelper != null) avatarHelper.destroy();
        super.onDestroy();
    }
}

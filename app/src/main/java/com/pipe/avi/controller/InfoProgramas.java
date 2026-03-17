package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
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

    ArrayList<String> programas;
    ArrayList<Integer> programIds;        // IDs de los programas
    ArrayList<Integer> recomendacionIds;  // IDs de la recomendación (idRECOMENDACION)

    int aspiranteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_programas);

        // Inicializar views
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

        // Recibir datos desde Resultados
        programas = getIntent().getStringArrayListExtra("programas");
        programIds = getIntent().getIntegerArrayListExtra("programIds");
        recomendacionIds = getIntent().getIntegerArrayListExtra("recomendacionIds"); // 🔥 recibir idRECOMENDACION

        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);
        System.out.println("ASPIRANTE ID EN INFO: " + aspiranteId);

        if (aspiranteId == 0) {
            Toast.makeText(this, "Error: aspiranteId no recibido", Toast.LENGTH_LONG).show();
        }

        // Llenar los programas en las vistas
        if (programas != null && !programas.isEmpty()) {
            if (programas.size() > 0) {
                llenarPrograma(programas.get(0), txtPrograma1, txtNivel1, txtDuracion1, txtDescripcion1);
            }
            if (programas.size() > 1) {
                llenarPrograma(programas.get(1), txtPrograma2, txtNivel2, txtDuracion2, txtDescripcion2);
            }
            if (programas.size() > 2) {
                llenarPrograma(programas.get(2), txtPrograma3, txtNivel3, txtDuracion3, txtDescripcion3);
            }
        }

        // Botón para ir a Mapa enviando todos los datos
        btnIrMapa.setOnClickListener(v -> {
            Intent intent = new Intent(InfoProgramas.this, Mapa.class);

            // Enviar nombres, IDs de programas y IDs de recomendación
            intent.putStringArrayListExtra("programas", programas);
            intent.putIntegerArrayListExtra("idPROGRAMA", programIds);
            intent.putIntegerArrayListExtra("recomendacionIds", recomendacionIds); // 🔥 enviar idRECOMENDACION
            intent.putExtra("aspiranteId", aspiranteId);

            startActivity(intent);
        });
    }

    private void llenarPrograma(String nombre,
                                TextView txtNombre,
                                TextView txtNivel,
                                TextView txtDuracion,
                                TextView txtDescripcion) {

        txtNombre.setText(nombre);

        String nivel;
        String duracion;

        String nombreLower = nombre.toLowerCase();
        if (nombreLower.contains("tecnico") || nombreLower.contains("técnico")) {
            nivel = "Nivel: Técnico";
            duracion = "Duración: 2 años";
        } else {
            nivel = "Nivel: Tecnólogo";
            duracion = "Duración: 3 años";
        }

        txtNivel.setText(nivel);
        txtDuracion.setText(duracion);

        txtDescripcion.setText(
                "Este programa de formación en " + nombre +
                        " permite desarrollar habilidades técnicas y prácticas " +
                        "en el área. Durante el proceso formativo aprenderás " +
                        "a aplicar conocimientos tecnológicos, resolver problemas " +
                        "del sector productivo y fortalecer competencias " +
                        "profesionales para tu desarrollo laboral."
        );
    }
}
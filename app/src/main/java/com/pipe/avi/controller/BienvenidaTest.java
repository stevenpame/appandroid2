package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

public class BienvenidaTest extends AppCompatActivity {

    Button btninittest;
    ImageButton btnhome, btnusuario;
    ProgressBar progressTest;

    int aspiranteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida_test);

        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);

        if (aspiranteId == 0) {
            Toast.makeText(this, "Error: ID no recibido", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        btnhome = findViewById(R.id.btnhome);
        btnusuario = findViewById(R.id.btnusuario);
        btninittest = findViewById(R.id.btninittest);
        progressTest = findViewById(R.id.progressTest);

        // Animación entrada de pantalla
        getWindow().getDecorView().startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.fade_in)
        );

        // -----------------------
        // BOTONES MENU
        // -----------------------

        btnhome.setOnClickListener(v -> {

            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.boton_press));

            Intent intent = new Intent(BienvenidaTest.this, Principal.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        btnusuario.setOnClickListener(v -> {

            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.boton_press));

            Intent intent = new Intent(BienvenidaTest.this, User.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });


        // -----------------------
        // INICIAR TEST
        // -----------------------

        btninittest.setOnClickListener(view -> {

            view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.boton_press));

            // Mostrar loader
            btninittest.setText("");
            progressTest.setVisibility(android.view.View.VISIBLE);
            btninittest.setEnabled(false);

            progressTest.startAnimation(
                    AnimationUtils.loadAnimation(this, R.anim.zoom_in)
            );

            view.postDelayed(() -> {

                Intent intent = new Intent(BienvenidaTest.this, PretestActivity.class);
                intent.putExtra("aspiranteId", aspiranteId);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }, 600); // pequeño delay para ver la animación
        });
    }
}
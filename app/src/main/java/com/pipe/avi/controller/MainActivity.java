package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.pipe.avi.R;

public class MainActivity extends AppCompatActivity {

    Button btniniciosesion, btnregistro;
    TextView titulo;
    ImageView gato;
    CardView cardBotones;

    Animation animBoton, animTitulo, animGato, animCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btniniciosesion = findViewById(R.id.btniniciosesion);
        btnregistro = findViewById(R.id.btnregistro);
        titulo = findViewById(R.id.textView);
        gato = findViewById(R.id.gato);
        cardBotones = findViewById(R.id.cardBotones);

        animBoton = AnimationUtils.loadAnimation(this, R.anim.boton_press);
        animTitulo = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animGato = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        animCard = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        titulo.startAnimation(animTitulo);
        gato.startAnimation(animGato);
        cardBotones.startAnimation(animCard);

        btniniciosesion.setOnClickListener(v -> {

            btniniciosesion.startAnimation(animBoton);

            Intent iniciosesion = new Intent(MainActivity.this, IniciarSesion.class);
            startActivity(iniciosesion);

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        btnregistro.setOnClickListener(v -> {

            btnregistro.startAnimation(animBoton);

            Intent registrar = new Intent(MainActivity.this, Registro.class);
            startActivity(registrar);

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }
}
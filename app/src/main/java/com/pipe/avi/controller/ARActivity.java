package com.pipe.avi.controller;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

public class ARActivity extends AppCompatActivity {

    boolean opened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // RECIBIR LA URL DEL AR DESDE EL INTENT
        String url = getIntent().getStringExtra("url_ar");

        // Si por alguna razón no llega la URL
        if (url == null || url.isEmpty()) {
            Toast.makeText(this, "No hay experiencia AR disponible.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(false);
        CustomTabsIntent customTabsIntent = builder.build();

        try {
            customTabsIntent.launchUrl(this, Uri.parse(url));
            opened = true;
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo abrir la experiencia AR.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Cierra la actividad cuando el usuario vuelve de la experiencia AR
        if (opened) {
            finish();
        }
    }
}
package com.pipe.avi.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.pipe.avi.R;
import com.pipe.avi.utils.CloudinaryManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class User extends AppCompatActivity {

    Button btncerrarsesion;
    Button btnCancelarPopup, btnGuardarPopup;

    ImageButton btnhome;

    LinearLayout LLeditperfil;

    ImageView imgPerfil;

    TextView txtCuenta, txtPreferencias;

    ConstraintLayout popupEditarPerfil;

    EditText editNombre, editCorreo, editTelefono;

    int aspiranteId;

    private static final int PICK_IMAGE = 1;

    Uri imageUri;

    String token;

    SharedPreferences prefs;

    Animation fade, slide, zoom, press, shake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        CloudinaryManager.init(this);

        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);

        prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        token = prefs.getString("TOKEN", "");

        btncerrarsesion = findViewById(R.id.btncerrarsesion);
        btnhome = findViewById(R.id.btnhome);

        LLeditperfil = findViewById(R.id.LLeditperfil);

        imgPerfil = findViewById(R.id.imgPerfil);

        txtCuenta = findViewById(R.id.textView4);
        txtPreferencias = findViewById(R.id.textView5);

        popupEditarPerfil = findViewById(R.id.popupEditarPerfil);

        btnCancelarPopup = findViewById(R.id.btnCancelarPopup);
        btnGuardarPopup = findViewById(R.id.btnGuardarPopup);

        editNombre = findViewById(R.id.editNombre);
        editCorreo = findViewById(R.id.editCorreo);
        editTelefono = findViewById(R.id.editTelefono);

        // Animaciones
        fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        slide = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        zoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        press = AnimationUtils.loadAnimation(this, R.anim.boton_press);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        // Animaciones iniciales
        txtCuenta.startAnimation(fade);
        txtPreferencias.startAnimation(fade);
        imgPerfil.startAnimation(zoom);

        LLeditperfil.postDelayed(() -> LLeditperfil.startAnimation(slide), 100);
        btncerrarsesion.postDelayed(() -> btncerrarsesion.startAnimation(slide), 200);
        btnhome.postDelayed(() -> btnhome.startAnimation(slide), 300);

        // 🔹 Cargar foto guardada
        String fotoGuardada = prefs.getString("FOTO_PERFIL", null);

        if (fotoGuardada != null) {

            new Thread(() -> {
                try {

                    InputStream input = new URL(fotoGuardada).openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);

                    runOnUiThread(() -> imgPerfil.setImageBitmap(bitmap));

                } catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        }

        // 📸 Cambiar foto
        imgPerfil.setOnClickListener(v -> {

            v.startAnimation(press);

            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            );

            startActivityForResult(intent, PICK_IMAGE);
        });

        // Abrir popup editar perfil
        LLeditperfil.setOnClickListener(v -> {

            v.startAnimation(press);

            popupEditarPerfil.setVisibility(View.VISIBLE);
            popupEditarPerfil.startAnimation(fade);

        });

        btnCancelarPopup.setOnClickListener(v -> {

            v.startAnimation(press);
            popupEditarPerfil.setVisibility(View.GONE);

        });

        // Guardar cambios
        btnGuardarPopup.setOnClickListener(v -> {

            v.startAnimation(press);

            String nombre = editNombre.getText().toString().trim();
            String correo = editCorreo.getText().toString().trim();
            String telefono = editTelefono.getText().toString().trim();

            if(nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty()){

                editNombre.startAnimation(shake);
                editCorreo.startAnimation(shake);
                editTelefono.startAnimation(shake);

                Toast.makeText(this,"Completa todos los campos",Toast.LENGTH_SHORT).show();
                return;
            }

            actualizarPerfil(nombre, correo, telefono);
        });

        // Cerrar sesión
        btncerrarsesion.setOnClickListener(v -> {

            v.startAnimation(press);

            new AlertDialog.Builder(User.this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        Intent intent = new Intent(User.this, MainActivity.class);
                        startActivity(intent);

                        overridePendingTransition(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                        );

                        finish();

                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // Home
        btnhome.setOnClickListener(v -> {

            v.startAnimation(press);

            Intent intent = new Intent(User.this, Principal.class);
            intent.putExtra("aspiranteId", aspiranteId);

            startActivity(intent);

            overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        });

    }

    private void actualizarPerfil(String nombre, String correo, String telefono) {

        new Thread(() -> {

            try {

                URL url = new URL(
                        "https://avibackcopia2-production.up.railway.app/api/perfilaspirante/" + aspiranteId
                );

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + token);

                conn.setDoOutput(true);

                String json =
                        "{ \"nombre\":\"" + nombre + "\", " +
                                "\"correo\":\"" + correo + "\", " +
                                "\"telefono\":\"" + telefono + "\" }";

                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes());
                os.flush();
                os.close();

                conn.getResponseCode();

                runOnUiThread(() -> {

                    popupEditarPerfil.setVisibility(View.GONE);
                    Toast.makeText(User.this,"Perfil actualizado correctamente",Toast.LENGTH_SHORT).show();

                });

            } catch (Exception e){
                e.printStackTrace();
            }

        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE &&
                resultCode == Activity.RESULT_OK &&
                data != null) {

            imageUri = data.getData();

            imgPerfil.setImageURI(imageUri);

            subirImagenCloudinary(imageUri);
        }
    }

    private void subirImagenCloudinary(Uri uri){

        MediaManager.get().upload(uri)
                .unsigned("android_upload")
                .callback(new UploadCallback() {

                    @Override
                    public void onStart(String requestId) {
                        Log.d("CLOUDINARY","Iniciando subida...");
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {}

                    @Override
                    public void onSuccess(String requestId, Map resultData) {

                        String imageUrl = resultData.get("secure_url").toString();

                        Log.d("CLOUDINARY","Imagen subida: "+imageUrl);

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("FOTO_PERFIL", imageUrl);
                        editor.apply();

                        runOnUiThread(() ->
                                Toast.makeText(User.this,"Foto actualizada",Toast.LENGTH_SHORT).show()
                        );

                        enviarFotoBackend(imageUrl);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {

                        Log.e("CLOUDINARY","Error: "+error.getDescription());

                        runOnUiThread(() ->
                                Toast.makeText(User.this,"Error al subir imagen",Toast.LENGTH_SHORT).show()
                        );
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {}
                }).dispatch();
    }

    private void enviarFotoBackend(String fotoUrl){

        new Thread(() -> {

            try {

                URL url = new URL(
                        "https://avibackcopia2-production.up.railway.app/api/aspirantes/" + aspiranteId
                );

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("PATCH");
                conn.setRequestProperty("Content-Type","application/json");
                conn.setRequestProperty("Authorization","Bearer "+token);

                conn.setDoOutput(true);

                String json = "{ \"foto\":\""+fotoUrl+"\" }";

                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes());
                os.flush();
                os.close();

                conn.getResponseCode();

            } catch (Exception e){
                e.printStackTrace();
            }

        }).start();
    }
}
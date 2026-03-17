package com.pipe.avi.controller;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pipe.avi.R;
import com.pipe.avi.model.Aspirante;
import com.pipe.avi.network.AspirantesApi;
import com.pipe.avi.network.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registro extends AppCompatActivity {

    EditText edtID, edtNombre, edtCorreo, edtTelefono,
            edtPass, edtBarrio, edtDireccion,
            edtInstitucion, edtFechaNacimiento;

    Spinner spnEstudios;
    Button btnregistrado;

    LinearLayout cardFormulario;

    Animation animCard, animBoton;

    String ocupacionSeleccionada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_registro);

        View scrollView = findViewById(R.id.scrollRegistro);

        ViewCompat.setOnApplyWindowInsetsListener(scrollView, (v, insets) -> {
            int imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
            v.setPadding(0, 0, 0, imeHeight);
            return insets;
        });

        // Inicialización de vistas
        edtID = findViewById(R.id.edtID);
        edtNombre = findViewById(R.id.edtNombre);
        edtFechaNacimiento = findViewById(R.id.edtFechaNacimiento);
        edtCorreo = findViewById(R.id.edtCorreo);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtBarrio = findViewById(R.id.edtBarrio);
        edtDireccion = findViewById(R.id.edtDireccion);
        edtPass = findViewById(R.id.edtPass);
        edtInstitucion = findViewById(R.id.edtInstitucion);
        spnEstudios = findViewById(R.id.spnEstudios);
        btnregistrado = findViewById(R.id.btnregistrado);

        cardFormulario = findViewById(R.id.cardFormulario);

        // Animaciones
        animCard = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        animBoton = AnimationUtils.loadAnimation(this, R.anim.boton_press);

        cardFormulario.startAnimation(animCard);

        configurarSpinner();
        configurarCalendario();

        btnregistrado.setOnClickListener(v -> {

            btnregistrado.startAnimation(animBoton);

            registrarAspirante();
        });
    }

    // =============================
    // 📅 CALENDARIO
    // =============================
    private void configurarCalendario() {

        edtFechaNacimiento.setFocusable(false);

        edtFechaNacimiento.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    Registro.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {

                        String fecha = String.format(Locale.getDefault(),
                                "%02d/%02d/%d",
                                selectedDay,
                                selectedMonth + 1,
                                selectedYear);

                        edtFechaNacimiento.setText(fecha);
                    },
                    year,
                    month,
                    day
            );

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
    }

    // =============================
    // SPINNER
    // =============================
    private void configurarSpinner() {

        String[] opciones = {"Seleccione una ocupación", "Colegio", "Universidad", "Otro"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                opciones
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEstudios.setAdapter(adapter);

        spnEstudios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    ocupacionSeleccionada = "";
                    edtInstitucion.setVisibility(View.GONE);
                } else {
                    ocupacionSeleccionada = opciones[position];
                    edtInstitucion.setVisibility(View.VISIBLE);

                    if (position == 1) {
                        edtInstitucion.setHint("Nombre del colegio");
                    } else if (position == 2) {
                        edtInstitucion.setHint("Nombre de la universidad");
                    } else {
                        edtInstitucion.setHint("Especifique");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    // =============================
    // REGISTRO
    // =============================
    private void registrarAspirante() {

        String idTexto = edtID.getText().toString().trim();
        String nombre = edtNombre.getText().toString().trim();
        String fechaNacimiento = edtFechaNacimiento.getText().toString().trim();
        String correo = edtCorreo.getText().toString().trim();
        String telefono = edtTelefono.getText().toString().trim();
        String barrio = edtBarrio.getText().toString().trim();
        String direccion = edtDireccion.getText().toString().trim();
        String contrasena = edtPass.getText().toString().trim();
        String institucion = edtInstitucion.getText().toString().trim();

        if (idTexto.isEmpty()) { edtID.setError("Obligatorio"); return; }
        if (nombre.isEmpty()) { edtNombre.setError("Obligatorio"); return; }
        if (fechaNacimiento.isEmpty()) { edtFechaNacimiento.setError("Obligatorio"); return; }
        if (correo.isEmpty()) { edtCorreo.setError("Obligatorio"); return; }
        if (telefono.isEmpty()) { edtTelefono.setError("Obligatorio"); return; }
        if (ocupacionSeleccionada.isEmpty()) {
            Toast.makeText(this, "Seleccione una ocupación", Toast.LENGTH_SHORT).show();
            return;
        }
        if (institucion.isEmpty()) { edtInstitucion.setError("Obligatorio"); return; }
        if (barrio.isEmpty()) { edtBarrio.setError("Obligatorio"); return; }
        if (direccion.isEmpty()) { edtDireccion.setError("Obligatorio"); return; }
        if (contrasena.isEmpty()) { edtPass.setError("Obligatorio"); return; }

        int id;
        try {
            id = Integer.parseInt(idTexto);
        } catch (NumberFormatException e) {
            edtID.setError("Debe ser numérico");
            return;
        }

        String fechaFormateada = "";
        try {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            Date fecha = formatoEntrada.parse(fechaNacimiento);
            fechaFormateada = formatoSalida.format(fecha);

        } catch (Exception e) {
            Toast.makeText(this, "Error en la fecha", Toast.LENGTH_SHORT).show();
            return;
        }

        Aspirante aspirante = new Aspirante(
                id,
                nombre,
                fechaFormateada,
                correo,
                telefono,
                barrio,
                direccion,
                ocupacionSeleccionada,
                institucion,
                contrasena
        );

        AspirantesApi api = RetrofitClient.getClient().create(AspirantesApi.class);
        Call<Aspirante> call = api.registrarAspirante(aspirante);

        call.enqueue(new Callback<Aspirante>() {
            @Override
            public void onResponse(Call<Aspirante> call, Response<Aspirante> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(Registro.this,
                            "Registro exitoso",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Registro.this, IniciarSesion.class);
                    startActivity(intent);

                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    finish();

                } else {
                    Toast.makeText(Registro.this,
                            "Error en el registro: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Aspirante> call, Throwable t) {
                Toast.makeText(Registro.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
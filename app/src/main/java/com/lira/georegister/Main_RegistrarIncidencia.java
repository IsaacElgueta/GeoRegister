package com.lira.georegister;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Main_RegistrarIncidencia extends AppCompatActivity {

    //Variables
    EditText etTitulo, etDescripcion, etFechaHora, etLatitud, etLongitud;
    Spinner spinnerCategoria;
    Button btnObtenerUbicacion, btnGuardar;

    // Variables para GPS y Base de datos
    FusedLocationProviderClient fusedLocationClient;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_registrar_incidencia);

        // 2. Conectar las variables con el XML
        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etFechaHora = findViewById(R.id.etFechaHora);
        etLatitud = findViewById(R.id.etLatitud);
        etLongitud = findViewById(R.id.etLongitud);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnObtenerUbicacion = findViewById(R.id.btnObtenerUbicacion);
        btnGuardar = findViewById(R.id.btnGuardar);

        ArrayAdapter<CharSequence> categoriasAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.categorias_incidencia,
                android.R.layout.simple_spinner_item
        );
        categoriasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(categoriasAdapter);

        //Inicializar Firebase y el GPS
        db = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Lógica del Botón de Ubicación (GPS)
        btnObtenerUbicacion.setOnClickListener(v -> {
            obtenerUbicacion();
        });

        // 5. Lógica del Botón Guardar
        btnGuardar.setOnClickListener(v -> {
            guardarIncidencia();
        });
    }

    // --- FUNCIÓN PARA PEDIR Y OBTENER LA UBICACIÓN ---
    private void obtenerUbicacion() {
        // Primero, verificar si tenemos permiso
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si no hay permiso, lo pedimos
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }

        // Si ya hay permiso, obtenemos la última ubicación conocida
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                // Si encontró ubicación, la mostramos en las cajas
                etLatitud.setText(String.valueOf(location.getLatitude()));
                etLongitud.setText(String.valueOf(location.getLongitude()));
                Toast.makeText(this, "Ubicación obtenida", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación. Activa el GPS.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- FUNCIÓN PARA GUARDAR EN FIREBASE ---
    private void guardarIncidencia() {
        // Obtener lo que escribió el usuario
        String titulo = etTitulo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String fechaHora = etFechaHora.getText().toString().trim();
        String categoria = spinnerCategoria.getSelectedItem() != null
                ? spinnerCategoria.getSelectedItem().toString()
                : "";
        String latitud = etLatitud.getText().toString().trim();
        String longitud = etLongitud.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (titulo.isEmpty() || descripcion.isEmpty() || latitud.isEmpty() || longitud.isEmpty()) {
            Toast.makeText(this, "Por favor, completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            return; // Detiene la función aquí
        }

        final double latitudNumero;
        final double longitudNumero;
        try {
            latitudNumero = Double.parseDouble(latitud);
            longitudNumero = Double.parseDouble(longitud);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Latitud y longitud deben ser números válidos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un "paquete" de datos para enviar a Firebase
        Map<String, Object> incidencia = new HashMap<>();
        incidencia.put("titulo", titulo);
        incidencia.put("descripcion", descripcion);
        incidencia.put("fecha", fechaHora);
        incidencia.put("categoria", categoria);
        incidencia.put("latitud", latitudNumero);
        incidencia.put("longitud", longitudNumero);

        // Enviar a la base de datos en la colección "incidencias"
        db.collection("incidencias").add(incidencia)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "¡Incidencia guardada exitosamente!", Toast.LENGTH_SHORT).show();
                    finish(); // Cierra la pantalla y vuelve al menú
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    // --- ESTO ES OBLIGATORIO PARA EL GPS ---
    // Maneja lo que pasa si el usuario acepta o deniega el permiso
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacion(); // Si aceptó, vuelve a intentar obtener ubicación
            } else {
                Toast.makeText(this, "Permiso denegado. No se puede obtener ubicación.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
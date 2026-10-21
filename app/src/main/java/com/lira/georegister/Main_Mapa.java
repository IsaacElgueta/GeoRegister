package com.lira.georegister;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;

public class Main_Mapa extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mapa);

        // Busca el mapa y lo enciende
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        FirebaseFirestore.getInstance()
                .collection("incidencias")
                .get()
                .addOnSuccessListener(resultado -> {
                    boolean hayMarcadores = false;
                    LatLng primeraUbicacion = null;

                    for (com.google.firebase.firestore.DocumentSnapshot documento
                            : resultado.getDocuments()) {
                        Double latitud = documento.getDouble("latitud");
                        Double longitud = documento.getDouble("longitud");

                        if (latitud == null || longitud == null
                                || latitud < -90 || latitud > 90
                                || longitud < -180 || longitud > 180) {
                            continue;
                        }

                        LatLng ubicacion = new LatLng(latitud, longitud);
                        if (primeraUbicacion == null) {
                            primeraUbicacion = ubicacion;
                        }

                        String titulo = documento.getString("titulo");
                        String categoria = documento.getString("categoria");
                        String snippet = categoria == null ? "" : "Categoría: " + categoria;

                        googleMap.addMarker(new MarkerOptions()
                                .position(ubicacion)
                                .title(titulo == null || titulo.trim().isEmpty()
                                        ? "Incidencia"
                                        : titulo)
                                .snippet(snippet));
                        hayMarcadores = true;
                    }

                    if (hayMarcadores && primeraUbicacion != null) {
                        googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(primeraUbicacion, 14f)
                        );
                    } else {
                        Toast.makeText(this, "No hay incidencias con ubicación válida",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(error -> Toast.makeText(
                        this,
                        "No se pudieron cargar los puntos: " + error.getLocalizedMessage(),
                        Toast.LENGTH_LONG
                ).show());
    }
}
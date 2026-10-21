package com.lira.georegister;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

public class Main_Historial extends AppCompatActivity {

    private IncidenciaAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_historial);

        ImageButton btnVolver = findViewById(R.id.imageButton3);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewHistorial);
        progressBar = findViewById(R.id.progressHistorial);
        tvEstado = findViewById(R.id.tvEstadoHistorial);

        btnVolver.setOnClickListener(v -> finish());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IncidenciaAdapter();
        recyclerView.setAdapter(adapter);

        cargarIncidencias();
    }

    private void cargarIncidencias() {
        progressBar.setVisibility(View.VISIBLE);
        tvEstado.setVisibility(View.GONE);

        FirebaseFirestore.getInstance()
                .collection("incidencias")
                .get()
                .addOnSuccessListener(resultado -> {
                    progressBar.setVisibility(View.GONE);
                    adapter.setIncidencias(resultado.getDocuments());
                    if (resultado.isEmpty()) {
                        tvEstado.setText(R.string.historial_vacio);
                        tvEstado.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(error -> {
                    progressBar.setVisibility(View.GONE);
                    tvEstado.setText(getString(
                            R.string.historial_error,
                            error.getLocalizedMessage()
                    ));
                    tvEstado.setVisibility(View.VISIBLE);
                });
    }
}
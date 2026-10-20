package com.lira.georegister;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    //variables
    Button btnRegistrar, btnHistorial, btnMapa, btnSensonres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //conexiones
        btnRegistrar = findViewById(R.id.buttonRegistrarIncidencia);
        btnHistorial = findViewById(R.id.buttonHistorial);
        btnMapa = findViewById(R.id.buttonMapa);
        btnSensonres = findViewById(R.id.buttonSensores);

        //Metodos para unirse a los demas entornos
        btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(this, Main_RegistrarIncidencia.class);
            startActivity(intent);
        });
        btnHistorial.setOnClickListener(v -> {
            Intent intent = new Intent(this, Main_Historial.class);
            startActivity(intent);
        });
        btnMapa.setOnClickListener(v -> {
            Intent intent = new Intent(this, Main_Mapa.class);
            startActivity(intent);
        });
        btnSensonres.setOnClickListener(v -> {
            Intent intent = new Intent(this, Main_Sensores.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}
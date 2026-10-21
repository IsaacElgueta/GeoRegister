package com.lira.georegister;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Main_Sensores extends AppCompatActivity implements SensorEventListener {

    // 1. Declarar los TextViews de la pantalla
    TextView tvX, tvY, tvZ, tvMagnitud, tvEstado, tvContador;

    // 2. Variables para el sensor
    SensorManager sensorManager;
    Sensor acelerometro;

    // Variables de lógica
    int contadorMovimientos = 0;
    boolean enMovimiento = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Asegúrate de que este sea el nombre de tu archivo XML
        setContentView(R.layout.activity_main_sensores);

        // 3. Conectar los TextView (Deben coincidir con los IDs de tu XML)
        tvX = findViewById(R.id.textViewX);
        tvY = findViewById(R.id.textViewY);
        tvZ = findViewById(R.id.textViewZ);
        tvMagnitud = findViewById(R.id.textViewMagnitud);
        tvEstado = findViewById(R.id.textViewEstado);
        tvContador = findViewById(R.id.textViewContador);

        // 4. Iniciar el sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // 5. Verificar si el celular tiene acelerómetro
        if (acelerometro == null) {
            tvEstado.setText("Sin sensor");
            tvEstado.setBackgroundColor(Color.RED);
        } else {
            tvEstado.setText("Reposo");
            tvEstado.setBackgroundColor(Color.parseColor("#4CAF50")); // Verde
        }
    }

    // Encender el sensor cuando la pantalla está visible
    @Override
    protected void onResume() {
        super.onResume();
        if (acelerometro != null) {
            sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    // Apagar el sensor cuando sales de la pantalla
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    // 6. Esta función se ejecuta cada vez que mueves el teléfono
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Obtener los valores X, Y, Z
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // Mostrar los valores con 1 decimal (como en tu imagen)
        tvX.setText("X: " + String.format("%.1f", x));
        tvY.setText("Y: " + String.format("%.1f", y));
        tvZ.setText("Z: " + String.format("%.1f", z));

        // Calcular la magnitud total (Raíz cuadrada de X² + Y² + Z²)
        double magnitud = Math.sqrt((x * x) + (y * y) + (z * z));
        tvMagnitud.setText(String.format("Magnitud Total : %.2f m/s²", magnitud));

        // 7. Lógica para detectar movimiento (mayor a 12 o menor a 7)
        if (magnitud > 12.0 || magnitud < 7.0) {

            // Si recién empezó a moverse, actualizamos el contador
            if (!enMovimiento) {
                contadorMovimientos++;
                tvContador.setText("Movimientos detectados : " + contadorMovimientos);
                enMovimiento = true;
            }

            tvEstado.setText("¡Movimiento!");
            tvEstado.setBackgroundColor(Color.parseColor("#EF5350")); // Rojo claro

        } else {
            // Si vuelve al reposo
            tvEstado.setText("Reposo");
            tvEstado.setBackgroundColor(Color.parseColor("#4CAF50")); // Verde
            enMovimiento = false;
        }
    }

    // Método obligatorio (no lo usamos pero debe estar)
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
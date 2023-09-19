package com.example.aplicationmedicale;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


public class HeartReatActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private TextView heartRateTextView;
    private Button goToPrincipal;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_reate);

        heartRateTextView = findViewById(R.id.heartRateTextView);
        goToPrincipal = findViewById(R.id.goToPrincipal);

        // Initialize the sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Check if the heart rate sensor is available
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        if (heartRateSensor == null) {
            // Handle the case when the heart rate sensor is not available
            heartRateTextView.setText("Heart rate sensor not found");
        }

        goToPrincipal.setOnClickListener(v -> {
            startActivity(new Intent(HeartReatActivity.this, PrincipalActivity.class));
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the sensor listener
        if (heartRateSensor != null) {
            sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the sensor listener to save battery
        if (heartRateSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            float heartRate = event.values[0];
            // Update your UI with the heart rate data
            heartRateTextView.setText("Heart Rate: " + heartRate);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }
}

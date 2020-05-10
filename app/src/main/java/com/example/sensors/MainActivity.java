package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor Accelerometer;
    private Sensor light;
    private Sensor gyro;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private TextView lightValue;
    private TextView gyroValue;
    private Button getGPSBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        Accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        light = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        gyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1 = (EditText) findViewById(R.id.editText);
        et2 = (EditText) findViewById(R.id.editText2);
        et3 = (EditText) findViewById(R.id.editText3);
        lightValue = (TextView) findViewById(R.id.light);
        gyroValue = (TextView) findViewById(R.id.textView2);
        getGPSBtn = (Button) findViewById(R.id.button);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        getGPSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSTracker g = new GPSTracker(getApplicationContext());
                Location l = g.getLocation();
                if(l!=null) {
                    Toast.makeText(getApplicationContext(), "LAT: " + l.getLatitude() + " LONG: " + l.getLongitude(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, Accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        if(light != null) {
            mSensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            lightValue.setText("Light sensor not supported");
        }

        if(gyro != null) {
            mSensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            gyroValue.setText("Gyroscope sensor not supported");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float sensorX = event.values[0];
            float sensorY = event.values[1];
            float sensorZ = event.values[2];

            et1.setText(String.valueOf(sensorX));
            et2.setText(String.valueOf(sensorY));
            et3.setText(String.valueOf(sensorZ));

            secretFeature(sensorX);
        }
        else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            lightValue.setText("Light Intensity: " + event.values[0]);
        }
        else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroValue.setText("Gyroscope value: " + event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void secretFeature(float sensorX) {
        if(sensorX >= 6.0) {
            Intent i = new Intent(this, Message.class);
            startActivity(i);
        }
    }
}

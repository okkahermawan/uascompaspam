package com.example.uascompas;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Compas extends AppCompatActivity implements SensorEventListener {

    private TextView textView;
    private ImageView imageView;
    private EditText editText;
    private Button add_button;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor, magnetometerSensor;

    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];

    boolean isLastAccelerometerArratCopied = false;
    boolean isLastMagnetometerArratCopied = false;

    long lastUpdateTime = 0;
    float curretDegree = 0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compas);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        textView = findViewById(R.id.value1);
        imageView = findViewById(R.id.compas);
        editText = findViewById(R.id.catatan);
        add_button = findViewById(R.id.log);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(Compas.this);
                myDB.addDerajat(getCurrentDate().trim(),editText.getText().toString().trim());

            }
        });
        sensorManager =(SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor == accelerometerSensor){
            System.arraycopy(event.values, 0,lastAccelerometer,0,event.values.length);
            isLastAccelerometerArratCopied = true;
        }else if (event.sensor == magnetometerSensor){
            System.arraycopy(event.values, 0,lastMagnetometer,0,event.values.length);
            isLastMagnetometerArratCopied = true;
        }

        if(isLastAccelerometerArratCopied && isLastMagnetometerArratCopied && System.currentTimeMillis() - lastUpdateTime>250){
            SensorManager.getRotationMatrix(rotationMatrix, null,lastAccelerometer,lastMagnetometer);
            SensorManager.getOrientation(rotationMatrix,orientation);

            float azimuthInRadians = orientation[0];
            float azimuthInDegree = (float) Math.toDegrees(azimuthInRadians);

            RotateAnimation rotateAnimation =
                    new RotateAnimation(curretDegree, -azimuthInDegree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
            rotateAnimation.setDuration(250);
            rotateAnimation.setFillAfter(true);
            imageView.startAnimation(rotateAnimation);

            curretDegree = -azimuthInDegree;
            lastUpdateTime = System.currentTimeMillis();

            int x = (int) azimuthInDegree;
            textView.setText(x + "°");

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, accelerometerSensor);
        sensorManager.unregisterListener(this, magnetometerSensor);
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();

        return dateFormat.format(date);
    }
}
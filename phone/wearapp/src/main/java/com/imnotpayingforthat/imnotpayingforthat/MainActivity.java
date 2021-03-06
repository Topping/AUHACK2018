package com.imnotpayingforthat.imnotpayingforthat;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends WearableActivity implements SensorEventListener {

    private final static String TAG = "Wear MainActivity";
    private String datapath = "/data_path";

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 1800;
    private ConstraintLayout constraintLayout;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enables Always-on
        setAmbientEnabled();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        constraintLayout = findViewById(R.id.mainbackground);
        random = new Random();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Wearable.getDataClient(this).addListener(this);
    }

    //receive data from the path.
//    @Override
//    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
//        Log.d(TAG, "onDataChanged: " + dataEventBuffer);
//        for (DataEvent event : dataEventBuffer) {
//            if (event.getType() == DataEvent.TYPE_CHANGED) {
//                String path = event.getDataItem().getUri().getPath();
//                if (datapath.equals(path)) {
//                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
//                    String message = dataMapItem.getDataMap().getString("message");
//                    Log.v(TAG, "Wear activity received message: " + message);
//                    // Display message in UI
//                } else {
//                    Log.e(TAG, "Unrecognized path: " + path);
//                }
//            } else if (event.getType() == DataEvent.TYPE_DELETED) {
//                Log.v(TAG, "Data deleted : " + event.getDataItem().toString());
//            } else {
//                Log.e(TAG, "Unknown data event Type = " + event.getType());
//            }
//        }
//    }



    private void sendData(String message) {
        PutDataMapRequest dataMap = PutDataMapRequest.create(datapath);
        dataMap.getDataMap().putString("message", message);
        PutDataRequest request = dataMap.asPutDataRequest();
        request.setUrgent();

        Task<DataItem> dataItemTask = Wearable.getDataClient(this).putDataItem(request);
        dataItemTask
                .addOnSuccessListener(new OnSuccessListener<DataItem>() {
                    @Override
                    public void onSuccess(DataItem dataItem) {
                        Log.d(TAG, "Sending message was successful: " + dataItem);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Sending message failed: " + e);
                    }
                })
        ;
    }

    public void ButtonClicked(View view) {
//        Toast.makeText(this, "KNOCK THE TABLE!", Toast.LENGTH_SHORT).show();
        vibrate(75);
        transisiton(0, randomColor(), 850);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = (last_x + last_y + last_z - x - y - z) / diffTime * 10000;

                if (speed >= SHAKE_THRESHOLD) {
                    DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm:ss");
                    String date = df.format(Calendar.getInstance().getTime());
                    sendData("Fahlberg gives beer at this time " + date);
                    Toast succes = Toast.makeText(this, "SUCCES", Toast.LENGTH_SHORT);
                    succes.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0,12);
                    succes.show();
                    mSensorManager.unregisterListener(this);
                    transisiton(Color.GREEN, Color.DKGRAY, 1000);
                    vibrate(1000);
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

//    public void transisiton(int color1, int color2, int duration)
//    {
//        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), color1, color2);
//        colorAnimation.setDuration(duration); // milliseconds
//        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//            @Override
//            public void onAnimationUpdate(ValueAnimator animator) {
//                constraintLayout.setBackgroundColor((int) animator.getAnimatedValue());
//            }
//
//        });
//        colorAnimation.start();
//    }

    public void transisiton(int color1, int color2, int duration)
    {
        if(color1 == 0)
        {
            color1 = Color.TRANSPARENT;
            Drawable background = constraintLayout.getBackground();
            if (background instanceof ColorDrawable)
            {
                color1 = ((ColorDrawable) background).getColor();
            }

        }

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), color1, color2);
        colorAnimation.setDuration(duration); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                constraintLayout.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }



    public void vibrate(int duration)
    {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 26
            v.vibrate(duration);
        }
    }

    public int randomColor()
    {
        return Color.argb(255, random.nextInt(255), random.nextInt(100), random.nextInt(255));
    }

}

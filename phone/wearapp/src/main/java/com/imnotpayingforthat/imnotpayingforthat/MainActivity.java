package com.imnotpayingforthat.imnotpayingforthat;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
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

public class MainActivity extends WearableActivity implements SensorEventListener {

    private final static String TAG = "Wear MainActivity";
    private String datapath = "/data_path";

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enables Always-on
        setAmbientEnabled();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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
        Toast.makeText(this, "KNOCK THE TABLE!", Toast.LENGTH_SHORT).show();
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

                if (speed > SHAKE_THRESHOLD) {
                    DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm:ss");
                    String date = df.format(Calendar.getInstance().getTime());
                    sendData("Fahlberg gives beer at this time " + date);
                    Toast.makeText(this, "GAVE A ROUND", Toast.LENGTH_SHORT).show();
                    mSensorManager.unregisterListener(this);

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


}

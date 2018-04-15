package com.imnotpayingforthat.imnotpayingforthat.services.register;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.imnotpayingforthat.imnotpayingforthat.R;

public class ListenService extends Service implements DataClient.OnDataChangedListener {
    private String TAG = "ListenService";
    String datapath = "/data_path";
    public static final int NOTIFICATION_ID = 1;
    private ListenBinder listenBinder = new ListenBinder();

    public ListenService() {
    }

    public class ListenBinder extends Binder {
        public ListenService getService(){ return ListenService.this;}
    }

    @Override
    public IBinder onBind(Intent intent) {
        return listenBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, notificationBuilder("No round yet"));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Wearable.getDataClient(this).removeListener(this);
    }

    public void stopService(){
        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
        Log.d(TAG, "onDataChanged: " + dataEventBuffer);
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (datapath.equals(path)) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    String message = dataMapItem.getDataMap().getString("message");
                    Log.v(TAG, "Wear activity received message: " + message);
                    // Display message in UI
                    makeNotification(message);
                    broadcastResult("GIVEROUND", message);
                } else {
                    Log.e(TAG, "Unrecognized path: " + path);
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.v(TAG, "Data deleted : " + event.getDataItem().toString());
            } else {
                Log.e(TAG, "Unknown data event Type = " + event.getType());
            }
        }
    }

    private void broadcastResult(String result, String message) {
        Intent intent = new Intent();
        intent.setAction(result);
        intent.putExtra("RoundMessage", message);
        sendBroadcast(intent);
    }

    private void makeNotification(String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

        Notification builder = notificationBuilder(message);
        notificationManager.notify(NOTIFICATION_ID, builder);
    }

    private Notification notificationBuilder(String message) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

        //https://developer.android.com/guide/topics/ui/notifiers/notifications.html
        //For working for sdk 26> and below.
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("WeatherChannelID", "Weather data", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        return new NotificationCompat.Builder(getApplicationContext(), "WeatherChannelID")
                .setContentTitle("ROUND MESSAGE")
                .setContentText(message)
                .setSmallIcon(R.mipmap.freeround_ic)
                .build();
    }
}

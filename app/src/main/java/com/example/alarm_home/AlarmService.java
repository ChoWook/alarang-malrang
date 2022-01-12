package com.example.alarm_home;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class AlarmService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Log.w("SERVICE", "startForeground");

            String channelId = createNotificationChannel();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
            Notification notification = builder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("알람입니다.")
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent intent1;
        intent.setAction(Intent.ACTION_VIEW);
        Log.w("SERVICE", intent.getStringExtra("service"));

        switch (intent.getIntExtra(MainActivity.ALARM_TYPE, 0)){
            case 1 : intent1 = new Intent(this, AlarmBasicActivity.class);
            break;
            case 2 : intent1 = new Intent(this, AlarmMissionActivity.class);
            break;
            default: intent1 = null;
            break;
        }
        new ContextWrapper(getApplicationContext()).startService(intent1);
        intent1.addFlags( /*Intent.FLAG_ACTIVITY_CLEAR_TOP | */Intent.FLAG_ACTIVITY_NEW_TASK /*| Intent.FLAG_ACTIVITY_REORDER_TO_FRONT*/);
        intent1.putExtra("activity", intent.getStringExtra("service"));
        startActivity(intent1);

        Log.w("SERVICE", "startActivity()");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            stopForeground(true);
        }

        stopSelf();

        return START_NOT_STICKY;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        String channelId = "Alarm";
        String channelName = getString(R.string.app_name);
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setSound(null,null);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        return  channelId;
    }
}

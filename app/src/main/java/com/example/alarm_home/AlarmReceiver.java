package com.example.alarm_home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent sIntent = new Intent(context, AlarmService.class);
        sIntent.putExtra("service", intent.getStringExtra("receiver"));
        sIntent.putExtra(MainActivity.ALARM_TYPE, intent.getIntExtra(MainActivity.ALARM_TYPE, 0));

        Log.w("RECEIVER", intent.getStringExtra("receiver"));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Log.w("RECEIVER", "FOREGROUND");
            context.startForegroundService(sIntent);
        }
        else{
            Log.w("RECEIVER", "BACKGROUND");
            context.startService(sIntent);
        }
    }
}

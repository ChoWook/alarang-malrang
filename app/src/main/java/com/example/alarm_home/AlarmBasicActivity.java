package com.example.alarm_home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class AlarmBasicActivity extends AppCompatActivity {

    AlarmItem alarmItem;
    AudioManager audioManager;
    MediaPlayer mediaPlayer;
    TextView textRingStop, textRingTitle, textRingDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_basic);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        alarmItem = new AlarmItem();
        Intent intent = getIntent();

        Log.w("AlarmBasicActivity", intent.getStringExtra("activity"));
        alarmItem.setAlarmItemString(intent.getStringExtra("activity"));

        textRingTitle = findViewById(R.id.txt_ring_title);
        textRingTitle.setText(alarmItem.title_promise);

        textRingDate = findViewById(R.id.txt_mission_ring_date);
        textRingDate.setText(alarmItem.getDate());

        textRingStop = findViewById(R.id.txtbtn_ring_stop);
        textRingStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
        try {
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if(alert == null){
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                if(alert == null) {
                    alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                }
            }
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), alert);
            //RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            try {
                mediaPlayer.setVolume((float) (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) / 7.0),
                        (float) (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) / 7.0));
            } catch (Exception e) {
                Log.w("ALARM_BASIC_ACTIVITY", e.getMessage());
            }
            mediaPlayer.start();
        }
        catch (Exception e){
            Log.w("AlarmBasicActivity", e.getMessage());
        }

        //(new AlarmIO()).offAlarm(alarmItem.index);
        //Log.w("OFF_ALARM_" + alarmItem.index, "FXXK");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private  void close(){
        if(this.mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        finish();
    }
}

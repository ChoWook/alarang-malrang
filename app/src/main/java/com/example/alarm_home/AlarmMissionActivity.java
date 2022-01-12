package com.example.alarm_home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class AlarmMissionActivity extends AppCompatActivity {

    AlarmItem alarmItem;
    AudioManager audioManager;
    MediaPlayer mediaPlayer;
    TextView textRingDate, textbtn_do_mission, textRingTitle;
    ImageView imgRingMission;
    Bitmap bitmapMission, bitmapSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_mission);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        alarmItem = new AlarmItem();
        Intent intent = getIntent();

        Log.w("AlarmBasicActivity", intent.getStringExtra("activity"));
        alarmItem.setAlarmItemString(intent.getStringExtra("activity"));

        //textRingTitle = findViewById(R.id.txt_mission_ring_title);
        //textRingTitle.setText(alarmItem.title_promise);

        imgRingMission = findViewById(R.id.img_ring_mission);
        try{
            bitmapMission = BitmapFactory.decodeFile(getApplicationContext().getFilesDir().getPath() + "/" + alarmItem.missionFileName);
            if(bitmapMission != null){
                imgRingMission.setImageBitmap(bitmapMission);
            }
            else{
                imgRingMission.setImageResource(R.drawable.mission_photo);
            }
        }

        catch (Exception e){
            Log.e("BITMAP", e.getMessage());
        }
        textRingDate = findViewById(R.id.txt_mission_ring_date);
        textRingDate.setText(alarmItem.getDate());

        textbtn_do_mission = findViewById(R.id.txtbtn_do_mission);
        textbtn_do_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(camera_intent.resolveActivity(getPackageManager()) != null){
                        startActivityForResult(camera_intent, MakeAlarm.CAM_REQUEST);
                    }
                }
                catch (Exception e){
                    Log.w("CAMERA", e.getMessage());
                }
                //close();
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

        (new AlarmIO()).offAlarm(alarmItem.index);
        Log.w("OFF_ALARM_" + alarmItem.index, "FXXK");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mediaPlayer != null){
            //mediaPlayer.release();
            //mediaPlayer = null;
        }
    }

    private  void close(){
        if(mediaPlayer != null && this.mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == MakeAlarm.CAM_REQUEST && resultCode == RESULT_OK){

            Bundle extras = data.getExtras();
            bitmapSubmit = (Bitmap) extras.get("data");

            int widthMission = bitmapMission.getWidth();
            int heightMission = bitmapMission.getHeight();
            int widthSubmit = bitmapSubmit.getWidth();
            int heightSubmit = bitmapSubmit.getHeight();

            if(widthMission != widthSubmit || heightMission != heightSubmit){
                return;
            }

            long diff = 0;

            for (int y = 0; y < heightMission; y++) {
                for (int x = 0; x < widthMission; x++) {
                    diff += pixelDiff(bitmapMission.getPixel(x, y), bitmapSubmit.getPixel(x, y));
                }
            }
            long maxDiff = 3L * 255 * widthMission * heightMission;

            DecimalFormat df = new DecimalFormat("#.###");
            Toast.makeText(getApplicationContext(), df.format(100.0 * diff / maxDiff), Toast.LENGTH_LONG).show();
            if(100.0 * diff / maxDiff < 20){    // 여기 숫자로 차이 조절할 수 있음 ex. 차이가 50퍼센트보다 작으면 닫는다
                close();
            }
        }
    }

    private static int pixelDiff(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >>  8) & 0xff;
        int b1 =  rgb1        & 0xff;
        int r2 = (rgb2 >> 16) & 0xff;
        int g2 = (rgb2 >>  8) & 0xff;
        int b2 =  rgb2        & 0xff;
        return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
    }
}

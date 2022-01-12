package com.example.alarm_home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;




public class MainActivity extends AppCompatActivity {

    LinearLayout BaseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BaseLayout = findViewById(R.id.baseLayout);

    }

    public void newPage_set(View v){
        Intent intent = new Intent(getApplicationContext(), RingSet.class);
        startActivity(intent);
    }

    public void newPage_make(View v){
        Intent intent = new Intent(getApplicationContext(), MakeAlarm.class);
        startActivity(intent);
    }




}

package com.example.alarm_home;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;

import static com.example.alarm_home.R.layout.activity_ring_set;

public class RingSet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_ring_set);
    }

    public void onGoHomeClicked(View v)
    {
        finish();
    }
}

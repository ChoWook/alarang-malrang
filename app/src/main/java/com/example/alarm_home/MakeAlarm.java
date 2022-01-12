package com.example.alarm_home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MakeAlarm extends AppCompatActivity {


    ImageButton PopupSet;

    View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_alarm);

        PopupSet = findViewById(R.id.popup_set);

        PopupSet.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                dialogView = View.inflate(MakeAlarm.this, R.layout.activity_make_popup, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MakeAlarm.this);

                dlg.setView(dialogView);
                dlg.show();
            }
        });



    }

    public void onGoHomeClicked(View v)
    {
        finish();
    }
}

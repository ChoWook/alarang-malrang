package com.example.alarm_home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MakePopup extends AppCompatActivity {



    ImageButton search;
    TextView search_txt;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_popup);

        search=findViewById(R.id.Search);
        search_txt = findViewById(R.id.Search_Text);
    }

    public void onSearchClicked(View view){
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://map.kakao.com/"));
        startActivity(intent);
    }
}

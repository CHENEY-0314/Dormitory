package com.example.dormitory;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGHT = 2000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_layout);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(WelcomeActivity.this,Tabbar.class);
                WelcomeActivity.this.startActivity(mainIntent);
                WelcomeActivity.this.finish();
            }

        }, SPLASH_DISPLAY_LENGHT);
    }
}

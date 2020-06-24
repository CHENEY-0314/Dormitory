package com.example.dormitory;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dormitory.Student.Tabbar;

public class WelcomeActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGHT = 2000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  //隐藏标题栏

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_layout);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(WelcomeActivity.this, Tabbar.class);
                WelcomeActivity.this.startActivity(mainIntent);
                WelcomeActivity.this.finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }

        }, SPLASH_DISPLAY_LENGHT);
    }
}

package com.example.dormitory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 学生登录 + 管理员登录
        Button mBtnStuLog = findViewById(R.id.btn_stuLogin);
        Button mBtnAdmLog = findViewById(R.id.btn_admLogin);
        // 学生登录按钮点击事件
        mBtnStuLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // 管理员登录按钮点击事件
        mBtnAdmLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}

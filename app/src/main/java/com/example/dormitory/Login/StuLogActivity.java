package com.example.dormitory.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dormitory.R;
import com.example.dormitory.Tabbar;

public class StuLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_log);
        EditText sAccount = findViewById(R.id.stu_account);
        EditText sPassword = findViewById(R.id.stu_password);
        Button mBtnSLogin = findViewById(R.id.btn_sLogin);
        mBtnSLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StuLogActivity.this, Tabbar.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_LONG).show();
            }
        });
    }
}

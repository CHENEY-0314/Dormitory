package com.example.dormitory.Login;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dormitory.Administrator.AdmActivity;
import com.example.dormitory.R;
import com.example.dormitory.WelcomeActivity;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences mData1,mData2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //获得本地学生的轻量数据库
        mData1=getSharedPreferences("userdata",0);
        //获得本地管理员的轻量数据库
        mData2=getSharedPreferences("admdata",0);
        autoLogin();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 学生登录 + 管理员登录
        Button mBtnStuLog = findViewById(R.id.btn_stuLogin);
        Button mBtnAdmLog = findViewById(R.id.btn_admLogin);
        // 学生登录按钮点击事件
        mBtnStuLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,StuLogActivity.class);
                startActivity(intent);
            }
        });
        // 管理员登录按钮点击事件
        mBtnAdmLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AdmLogActivity.class);
                startActivity(intent);
            }
        });
    }

    private void autoLogin(){
        if(!mData1.getString("s_id","").equals("")){
//            Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
//            startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
        }
        else if(!mData2.getString("a_id","").equals("")){
//            Intent intent = new Intent(LoginActivity.this, AdmActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent intent = new Intent(LoginActivity.this, AdmActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
//            startActivity(new Intent(LoginActivity.this, AdmActivity.class));
        }
        else{

        }
    }
}

package com.example.dormitory.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dormitory.Administrator.admTabbar;
import com.example.dormitory.R;
import com.example.dormitory.WelcomeActivity;

public class AdmLogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_log);
        EditText aAccount = findViewById(R.id.adm_account);
        EditText aPassword = findViewById(R.id.adm_password);
        Button mBtnALogin = findViewById(R.id.btn_aLogin);
        mBtnALogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdmLogActivity.this, admTabbar.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

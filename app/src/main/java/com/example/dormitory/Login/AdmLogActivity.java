package com.example.dormitory.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dormitory.Administrator.AdmActivity;
import com.example.dormitory.R;

public class AdmLogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_log);
        EditText aAccount = findViewById(R.id.adm_account);
        EditText aPassword = findViewById(R.id.adm_password);
        Button mBtnALogin = findViewById(R.id.btn_aLogin);
        mBtnALogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdmLogActivity.this, AdmActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

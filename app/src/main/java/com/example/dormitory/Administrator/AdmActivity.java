package com.example.dormitory.Administrator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.dormitory.Administrator.ReleaseNote.ReleaseNoteActivity;
import com.example.dormitory.R;

public class AdmActivity extends AppCompatActivity {

    // 声明所有控件
    private LinearLayout admApplyMan, admQuery, admNoteRe, admLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adm_activity);
        init();
    }

    // 初始化所有控件，并绑定点击事件
    void init(){
        // 初始化控件
        admApplyMan = findViewById(R.id.admApplyMan);
        admQuery = findViewById(R.id.admQuery);
        admNoteRe = findViewById(R.id.admNoteRe);
        admLogout = findViewById(R.id.adm_logout);
        // 绑定点击事件
        admApplyMan.setOnClickListener(new clickListener());
        admQuery.setOnClickListener(new clickListener());
        admNoteRe.setOnClickListener(new clickListener());
        admLogout.setOnClickListener(new clickListener());
    }

    // 点击事件
    private class clickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.admApplyMan:// 申请管理点击事件

                    break;
                case R.id.admQuery:// 查询点击事件

                    break;
                case R.id.admNoteRe:// 发布通知点击事件
                    // 跳转到发布通知页面
                    Intent intent = new Intent(AdmActivity.this, ReleaseNoteActivity.class);
                    startActivity(intent);
                    break;
                case R.id.adm_logout:// 注销点击事件

                    break;
                default:
                    break;
            }
        }
    }

}

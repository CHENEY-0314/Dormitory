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
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

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
        // 绑定点击事件
        admApplyMan.setOnClickListener(new clickListener());
        admQuery.setOnClickListener(new clickListener());
        admNoteRe.setOnClickListener(new clickListener());
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
                default:
                    break;
            }
        }
    }

}

package com.example.dormitory.Administrator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dormitory.Administrator.ReleaseNote.ReleaseNoteActivity;
import com.example.dormitory.Login.LoginActivity;
import com.example.dormitory.R;
import com.example.dormitory.Administrator.ManageApply.admManageApply;

public class AdmActivity extends AppCompatActivity {

    // 声明所有控件
    private LinearLayout admApplyMan, admQuery, admNoteRe;
    private TextView admLogout,name,id;
    private SharedPreferences admData;
    private SharedPreferences.Editor admDataEditor;

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
        id=findViewById(R.id.MyPage_txt_home);
        name=findViewById(R.id.MyPage_txt_username);
        admApplyMan = findViewById(R.id.admApplyMan);
        admQuery = findViewById(R.id.admQuery);
        admNoteRe = findViewById(R.id.admNoteRe);
        admLogout=findViewById(R.id.adm_loginOut);
        // 绑定点击事件
        admApplyMan.setOnClickListener(new clickListener());
        admQuery.setOnClickListener(new clickListener());
        admNoteRe.setOnClickListener(new clickListener());
        admLogout.setOnClickListener(new clickListener());

       //id.setText(admData.getString("a_id","工号"));
       //name.setText(admData.getString("name","管理员"));
    }

    // 点击事件
    private class clickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.admApplyMan:// 申请管理点击事件
                    Intent intent2=new Intent(AdmActivity.this,admManageApply.class);
                    startActivity(intent2);
                    break;
                case R.id.admQuery:// 查询点击事件

                    break;
                case R.id.admNoteRe:// 发布通知点击事件
                    // 跳转到发布通知页面
                    Intent intent = new Intent(AdmActivity.this, ReleaseNoteActivity.class);
                    startActivity(intent);
                    break;
                case R.id.adm_loginOut://退出登录
                    logOut();
                    break;
                default:
                    break;
            }
        }
    }

    private void logOut(){
        System.out.println("点击账号注销");
        admData=getSharedPreferences("admdata",0);
        admDataEditor=admData.edit();
        final AlertDialog.Builder builder_loginOut=new AlertDialog.Builder(AdmActivity.this);
        builder_loginOut.setTitle("是否注销账号").setMessage("退出登录后需重新登录并加载数据").setCancelable(false);
        builder_loginOut.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                admDataEditor.clear();
                admDataEditor.commit();
                Intent intent = new Intent(AdmActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder_loginOut.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
        builder_loginOut.show();
    }

}

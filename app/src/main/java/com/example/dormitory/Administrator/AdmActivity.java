package com.example.dormitory.Administrator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dormitory.Administrator.ReleaseNote.ReleaseNoteActivity;
import com.example.dormitory.Login.LoginActivity;
import com.example.dormitory.R;
import com.example.dormitory.Administrator.ManageApply.admManageApply;

public class AdmActivity extends AppCompatActivity {

    // 声明所有控件
    private LinearLayout admApplyMan, admNoteRe;
    private TextView admLogout,id;
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
        admData=getSharedPreferences("admdata",MODE_PRIVATE);
        // 初始化控件
        id=findViewById(R.id.admNumb);
        admApplyMan = findViewById(R.id.admApplyMan);
        admNoteRe = findViewById(R.id.admNoteRe);
        admLogout=findViewById(R.id.adm_loginOut);
        // 绑定点击事件
        admApplyMan.setOnClickListener(new clickListener());
        admNoteRe.setOnClickListener(new clickListener());
        admLogout.setOnClickListener(new clickListener());
        id.setText("工号："+admData.getString("a_id","工号").toString());
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
    //管理员注销
    private void logOut() {
        System.out.println("点击账号注销");
        admData = getSharedPreferences("admdata", 0);
        admDataEditor = admData.edit();
        final Dialog dlg = new Dialog(AdmActivity.this);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dlg.show();
        Window window = dlg.getWindow();
        WindowManager windowManager = AdmActivity.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        lp.width = (int) (width - 200); // 设置宽度
        dlg.getWindow().setAttributes(lp);
        window.setContentView(R.layout.dialog_box_adm);
        // 为确认按钮添加事件,执行退出应用操作
        Button btnok = (Button) window.findViewById(R.id.btn_ok);
        Button btncancel = (Button) window.findViewById(R.id.btn_cancel);
        TextView text = (TextView) window.findViewById(R.id.dialog_box_text);
        TextView title = (TextView) window.findViewById(R.id.title);
        title.setText("是否注销登录");
        text.setText("退出登录后需重新登录并加载数据");
        btnok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                admDataEditor.clear();
                admDataEditor.commit();
                Intent intent = new Intent(AdmActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dlg.dismiss();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }

}

package com.example.dormitory.Student.MyPagesActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dormitory.R;

public class AdviceActivity extends AppCompatActivity {

    private EditText adtAdvice;
    private Button mbtnsubmit;
    private TextView mTextNum;
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);

        adtAdvice=findViewById(R.id.Advice_edt);
        mbtnsubmit=findViewById(R.id.Advice_btn);
        mTextNum=findViewById(R.id.Advice_num);
        mBack=findViewById(R.id.Advice_back);

        //监听输入框字数变化
        adtAdvice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {  //监听输入框字数变化
                mTextNum.setText(String.valueOf(s.length())+"/600");
                if(s.length()>30){
                    mbtnsubmit.setEnabled(true);
                }else mbtnsubmit.setEnabled(false);
            }
        });

        //点击提交按钮
        mbtnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //点击上方返回按钮
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdviceActivity.this.finish();
            }
        });

    }



}

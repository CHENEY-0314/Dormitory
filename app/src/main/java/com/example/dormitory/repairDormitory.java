package com.example.dormitory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class repairDormitory extends AppCompatActivity {
    //声明返回按钮的组件
    private ImageView mBack;
    private TextView mOtherNum;
    private EditText mEdtOther;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_dormitory);
        mBack=findViewById(R.id.repairDor_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repairDormitory.this.finish();
            }
        });

        mOtherNum=findViewById(R.id.repairDor_txt_OtherNum);
        mEdtOther=findViewById(R.id.repairDor_edt_Other);
        //监听输入框字数变化
        mEdtOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {  //监听输入框字数变化
                mOtherNum.setText(String.valueOf(s.length())+"/400");
            }
        });

    }
}

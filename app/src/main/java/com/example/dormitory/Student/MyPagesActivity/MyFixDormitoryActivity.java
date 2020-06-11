package com.example.dormitory.Student.MyPagesActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

import com.example.dormitory.R;

public class MyFixDormitoryActivity extends AppCompatActivity {

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fix_dormitory);

        back=findViewById(R.id.AMFD_back);
        back.setOnClickListener(new View.OnClickListener() {  //点击上方返回按钮
            @Override
            public void onClick(View v) {
                MyFixDormitoryActivity.this.finish();
            }
        });

    }
}

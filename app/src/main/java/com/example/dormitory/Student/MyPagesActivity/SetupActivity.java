package com.example.dormitory.Student.MyPagesActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dormitory.R;

public class SetupActivity extends AppCompatActivity {

    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mBack=findViewById(R.id.Setup_back);

        //点击上方返回按钮
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetupActivity.this.finish();
            }
        });

    }

}

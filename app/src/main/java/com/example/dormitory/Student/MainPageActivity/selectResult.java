package com.example.dormitory.Student.MainPageActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dormitory.R;

public class selectResult extends AppCompatActivity {
    //声明RecyclerView组件 返回按钮组件
    private RecyclerView mRvSelect;
    private ImageView mBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_result);
        //绑定列表组件
        mRvSelect=findViewById(R.id.select_recyclerview);
        //设置线性布局管理器
        mRvSelect.setLayoutManager(new LinearLayoutManager(selectResult.this));
        //设置分割线
        mRvSelect.addItemDecoration(new MyDecoration());
        //将适配器布置到组件上，适配器为自定义，见LinearAdapater.java文件
        mRvSelect.setAdapter(new LinearAdapater(selectResult.this));
        mBack=findViewById(R.id.selectRes_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectResult.this.finish();
            }
        });
    }

    //实现下分割线效果，因颜色和背景一样所以看不出来
    class MyDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect,view,parent,state);
            outRect.set(0,0,0,getResources().getDimensionPixelOffset(R.dimen.dividerHeight));
        }
    }
}

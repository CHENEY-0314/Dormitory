package com.example.dormitory.Student.MainPageActivity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.dormitory.R;

public class changeDormitory extends AppCompatActivity {
    //声明四个下拉框组件和图片组件
    private Spinner department;
    private Spinner building;
    private Spinner floor;
    private Spinner bed;
    private ImageView mBack;
    //定义二维字符串数组，用于选择楼栋随学院的动态变化
    private String[][] buildData=new String[][]{{"C10","C12"},{"C8","C9"},{"C5","C6"},{"C11","C9"},{"C11"},{"C7"},{"C13"},{"C3","C4"},{"C2","C3"}};
    //声明适配器，用于动态配置楼栋内容
    private ArrayAdapter<CharSequence> adapterBuilding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_dormitory);
        //绑定四个组件
        department=findViewById(R.id.department);
        building=findViewById(R.id.building);
        floor=findViewById(R.id.floor);
        bed=findViewById(R.id.bed);
        mBack=findViewById(R.id.changeDor_back);
        //监听“选择学院”的点击事件
        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //根据点击的学院，初始化楼栋内容的适配器
                adapterBuilding=new ArrayAdapter<CharSequence>(changeDormitory.this,android.R.layout.simple_spinner_dropdown_item,changeDormitory.this.buildData[position]);
                //设置弹出框的样式
                adapterBuilding.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //将适配器布置到building组件上
                building.setAdapter(adapterBuilding);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        building.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //弹窗点击事件
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        floor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //弹窗点击事件
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //弹窗点击事件
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //点击上方返回按钮
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDormitory.this.finish();
            }
        });

    }

}


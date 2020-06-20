package com.example.dormitory.Student.MainPageActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dormitory.R;

public class changeDormitory extends AppCompatActivity {
    //声明四个下拉框组件和图片组件
    private ImageView mBack;
    private Spinner department;
    private Spinner building;
    private CheckBox CheBox_floor_1,CheBox_floor_2,CheBox_floor_3,CheBox_floor_4,CheBox_floor_5,CheBox_floor_6,CheBox_floor_7,CheBox_floor_8;
    private CheckBox CheBox_bed_1,CheBox_bed_2,CheBox_bed_3,CheBox_bed_4,CheBox_bed_5;
    private Button BtnSubmit;
    private EditText editBuilding,editRoom;
    //本地数据库
    private SharedPreferences mUser;
    //定义二维字符串数组，用于选择楼栋随学院的动态变化
    private String[][] buildData=new String[][]{{"C10","C12"},{"C8","C9"},{"C5","C6"},{"C11","C9"},{"C11"},{"C7"},{"C13"},{"C3","C4"},{"C2","C3"},{"随意","C1","C2","C3","C4","C5","C6","C7","C8","C9","C10","C11","C12","C13","C14","C15"}};
    //声明适配器，用于动态配置楼栋内容
    private ArrayAdapter<CharSequence> adapterBuilding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_dormitory);
        //初始化
        init();
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
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    //初始化
    public void init(){
        //绑定组件
        mBack=findViewById(R.id.changeDor_back);
        department=findViewById(R.id.department);
        building=findViewById(R.id.building);
        CheBox_floor_1=findViewById(R.id.changeDor_floor_cb_1);
        CheBox_floor_2=findViewById(R.id.changeDor_floor_cb_2);
        CheBox_floor_3=findViewById(R.id.changeDor_floor_cb_3);
        CheBox_floor_4=findViewById(R.id.changeDor_floor_cb_4);
        CheBox_floor_5=findViewById(R.id.changeDor_floor_cb_5);
        CheBox_floor_6=findViewById(R.id.changeDor_floor_cb_6);
        CheBox_floor_7=findViewById(R.id.changeDor_floor_cb_7);
        CheBox_floor_8=findViewById(R.id.changeDor_floor_cb_8);
        CheBox_bed_1=findViewById(R.id.changeDor_bed_cb_1);
        CheBox_bed_2=findViewById(R.id.changeDor_bed_cb_2);
        CheBox_bed_3=findViewById(R.id.changeDor_bed_cb_3);
        CheBox_bed_4=findViewById(R.id.changeDor_bed_cb_4);
        CheBox_bed_5=findViewById(R.id.changeDor_bed_cb_5);
        BtnSubmit=findViewById(R.id.changeDor_submit);
        editBuilding=findViewById(R.id.changeDor_txt_building);
        editRoom=findViewById(R.id.changeDor_txt_room);
        //设置组件监听事件
        mBack.setOnClickListener(new ButtonListener());
        CheBox_floor_1.setOnClickListener(new ButtonListener());
        CheBox_floor_2.setOnClickListener(new ButtonListener());
        CheBox_floor_3.setOnClickListener(new ButtonListener());
        CheBox_floor_4.setOnClickListener(new ButtonListener());
        CheBox_floor_5.setOnClickListener(new ButtonListener());
        CheBox_floor_6.setOnClickListener(new ButtonListener());
        CheBox_floor_7.setOnClickListener(new ButtonListener());
        CheBox_floor_8.setOnClickListener(new ButtonListener());
        CheBox_bed_1.setOnClickListener(new ButtonListener());
        CheBox_bed_2.setOnClickListener(new ButtonListener());
        CheBox_bed_3.setOnClickListener(new ButtonListener());
        CheBox_bed_4.setOnClickListener(new ButtonListener());
        CheBox_bed_5.setOnClickListener(new ButtonListener());
        BtnSubmit.setOnClickListener(new ButtonListener());


        //给现住信息里面的楼号、宿舍号赋默认值
        mUser=getSharedPreferences("userdata",MODE_PRIVATE);
        editBuilding.setHint(mUser.getString("building","未获取"));
        editRoom.setHint(mUser.getString("room_num","未获取"));
    }
    //监听8+5共13个CheckBox、提交按钮和返回按钮的点击事件
    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.changeDor_floor_cb_1:
                case R.id.changeDor_floor_cb_2:
                case R.id.changeDor_floor_cb_3:
                case R.id.changeDor_floor_cb_4:
                case R.id.changeDor_floor_cb_5:
                case R.id.changeDor_floor_cb_6:
                case R.id.changeDor_floor_cb_7:{
                    CheBox_floor_8.setChecked(false);
                    //楼层和床位号每个至少选一个才可提交
                    if((CheBox_floor_1.isChecked()||CheBox_floor_2.isChecked()||CheBox_floor_3.isChecked()||CheBox_floor_4.isChecked()
                            ||CheBox_floor_5.isChecked()||CheBox_floor_6.isChecked()||CheBox_floor_7.isChecked()||CheBox_floor_8.isChecked())
                            &&(CheBox_bed_1.isChecked()|| CheBox_bed_2.isChecked()||CheBox_bed_3.isChecked()||CheBox_bed_4.isChecked()
                            ||CheBox_bed_5.isChecked())){
                        BtnSubmit.setEnabled(true);
                    }
                    else{
                        BtnSubmit.setEnabled(false);
                    }
                    break;
                }
                case R.id.changeDor_floor_cb_8:{
                    CheBox_floor_1.setChecked(false);
                    CheBox_floor_2.setChecked(false);
                    CheBox_floor_3.setChecked(false);
                    CheBox_floor_4.setChecked(false);
                    CheBox_floor_5.setChecked(false);
                    CheBox_floor_6.setChecked(false);
                    CheBox_floor_7.setChecked(false);
                    //楼层和床位号每个至少选一个才可提交
                    if((CheBox_floor_1.isChecked()||CheBox_floor_2.isChecked()||CheBox_floor_3.isChecked()||CheBox_floor_4.isChecked()
                            ||CheBox_floor_5.isChecked()||CheBox_floor_6.isChecked()||CheBox_floor_7.isChecked()||CheBox_floor_8.isChecked())
                            &&(CheBox_bed_1.isChecked()|| CheBox_bed_2.isChecked()||CheBox_bed_3.isChecked()||CheBox_bed_4.isChecked()
                            ||CheBox_bed_5.isChecked())){
                        BtnSubmit.setEnabled(true);
                    }
                    else{
                        BtnSubmit.setEnabled(false);
                    }
                    break;
                }
                case R.id.changeDor_bed_cb_1:
                case R.id.changeDor_bed_cb_2:
                case R.id.changeDor_bed_cb_3:
                case R.id.changeDor_bed_cb_4:{
                    CheBox_bed_5.setChecked(false);
                    //楼层和床位号每个至少选一个才可提交
                    if((CheBox_floor_1.isChecked()||CheBox_floor_2.isChecked()||CheBox_floor_3.isChecked()||CheBox_floor_4.isChecked()
                    ||CheBox_floor_5.isChecked()||CheBox_floor_6.isChecked()||CheBox_floor_7.isChecked()||CheBox_floor_8.isChecked())
                            &&(CheBox_bed_1.isChecked()|| CheBox_bed_2.isChecked()||CheBox_bed_3.isChecked()||CheBox_bed_4.isChecked()
                            ||CheBox_bed_5.isChecked())){
                        BtnSubmit.setEnabled(true);
                    }
                    else{
                        BtnSubmit.setEnabled(false);
                    }
                    break;
                }
                case R.id.changeDor_bed_cb_5:{
                    CheBox_bed_1.setChecked(false);
                    CheBox_bed_2.setChecked(false);
                    CheBox_bed_3.setChecked(false);
                    CheBox_bed_4.setChecked(false);
                    //楼层和床位号每个至少选一个才可提交
                    if((CheBox_floor_1.isChecked()||CheBox_floor_2.isChecked()||CheBox_floor_3.isChecked()||CheBox_floor_4.isChecked()
                            ||CheBox_floor_5.isChecked()||CheBox_floor_6.isChecked()||CheBox_floor_7.isChecked()||CheBox_floor_8.isChecked())
                            &&(CheBox_bed_1.isChecked()|| CheBox_bed_2.isChecked()||CheBox_bed_3.isChecked()||CheBox_bed_4.isChecked()
                            ||CheBox_bed_5.isChecked())){
                        BtnSubmit.setEnabled(true);
                    }
                    else{
                        BtnSubmit.setEnabled(false);
                    }
                    break;
                }
                case R.id.changeDor_submit:{
                    //跳转到筛选结果页面
                    Intent intent=new Intent(changeDormitory.this,selectResult.class);
                    startActivity(intent);
                    break;
                }
                case R.id.changeDor_back:{
                    //返回按钮直接关闭页面
                    changeDormitory.this.finish();
                    break;
                }
                default:break;
            }
        }
    }
}


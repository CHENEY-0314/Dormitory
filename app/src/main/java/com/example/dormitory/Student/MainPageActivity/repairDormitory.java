package com.example.dormitory.Student.MainPageActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dormitory.Login.StuLogActivity;
import com.example.dormitory.R;
import com.example.dormitory.WelcomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class repairDormitory extends AppCompatActivity {
    private ImageView mBack;//声明返回按钮的组件
    private TextView mOtherNum;//备注处字数组件
    private EditText mEdtOther,mEdtPhoneNum;//声明备注和联系方式组件
    private CheckBox mCheckBox1,mCheckBox2,mCheckBox3,mCheckBox4,mCheckBox5,mCheckBox6;//声明报修处6个组件
    private Button mBtnSubmit;//声明提交按钮组件
    private Boolean[] submitOrNot;//用于判断能否提交
    private EditText editBuilding,editRoom,editStuNum;//楼号、宿舍号、学号的编辑框控件
    private SharedPreferences mUser;//获取本地数据库
    private Boolean ifCanFixApply;//判断是否可以进行报修申请
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_dormitory);
        //初始化函数包括绑定控件、声明监听事件、返回事件、初始化submitOrNot布尔数组、初始化楼号 宿舍号 学号这三个默认初值
        init();
        InputFilter[] filters = {new InputFilter.LengthFilter(11)};
        mEdtPhoneNum.setFilters(filters);
        //监听手机号框位数变化
        mEdtPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==11){
                    //输入11位手机号，此部分信息完整，设置为true
                    submitOrNot[1]=true;
                }
                else{
                    //不完整则设为false
                    submitOrNot[1]=false;
                }
                //三部分信息均完整，则可提交
                if(submitOrNot[0]&&submitOrNot[1]&&submitOrNot[2]){
                    mBtnSubmit.setEnabled(true);
                }
                else{
                    //信息不完整，无法提交
                    mBtnSubmit.setEnabled(false);
                }
            }
        });

        //监听输入框字数变化
        mEdtOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {  //监听输入框字数变化
                mOtherNum.setText(String.valueOf(s.length())+"/400");
                if(s.length()>=5){
                    //字数大于5，信息完整，此部分设为true
                    submitOrNot[2]=true;
                }
                else{
                    //信息不完整，此部分为false
                    submitOrNot[2]=false;
                }
                //信息完整可提交
                if(submitOrNot[0]&&submitOrNot[1]&&submitOrNot[2]){
                    mBtnSubmit.setEnabled(true);
                }
                else{
                    //不可提交
                    mBtnSubmit.setEnabled(false);
                }
            }
        });

    }
    //初始化函数
    public void init(){
        //报修类别、联系方式、备注三个是否完整用true、false代表
        submitOrNot=new Boolean[3];
        submitOrNot[0]=true;
        submitOrNot[1]=submitOrNot[2]=false;
        //绑定声明的控件
        mBack=findViewById(R.id.repairDor_back);
        mOtherNum=findViewById(R.id.repairDor_txt_OtherNum);
        mEdtOther=findViewById(R.id.repairDor_edt_Other);
        mCheckBox1=findViewById(R.id.repairDor_cb_1);
        mCheckBox2=findViewById(R.id.repairDor_cb_2);
        mCheckBox3=findViewById(R.id.repairDor_cb_3);
        mCheckBox4=findViewById(R.id.repairDor_cb_4);
        mCheckBox5=findViewById(R.id.repairDor_cb_5);
        mCheckBox6=findViewById(R.id.repairDor_cb_6);
        mBtnSubmit=findViewById(R.id.repairDor_submit);
        mEdtPhoneNum=findViewById(R.id.repairDor_edt_Number);
        editBuilding=findViewById(R.id.repairDor_txt_building);
        editRoom=findViewById(R.id.repairDor_txt_room);
        editStuNum=findViewById(R.id.repairDor_txt_studentNum);

        //mBack设置点击返回事件
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repairDormitory.this.finish();
            }
        });
        //6个CheckBox设置点击监听事件
        mCheckBox1.setOnClickListener(new ButtonListener());
        mCheckBox2.setOnClickListener(new ButtonListener());
        mCheckBox3.setOnClickListener(new ButtonListener());
        mCheckBox4.setOnClickListener(new ButtonListener());
        mCheckBox5.setOnClickListener(new ButtonListener());
        mCheckBox6.setOnClickListener(new ButtonListener());
        mBtnSubmit.setOnClickListener(new ButtonListener());

        //给楼号、宿舍号、学号赋默认初值
        mUser=getSharedPreferences("userdata",MODE_PRIVATE);
        editBuilding.setHint(mUser.getString("building","未获取"));
        editRoom.setHint(mUser.getString("room_num","未获取"));
        editStuNum.setHint(mUser.getString("s_id","未获取"));

        //判断能否进行报修申请，并对相应的布尔值ifCanFixApply赋值
        whetherSubmit();
    }

    //监听6个CheckBox和提交按钮的点击事件
    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.repairDor_cb_1:
                case R.id.repairDor_cb_2:
                case R.id.repairDor_cb_3:
                case R.id.repairDor_cb_4:
                case R.id.repairDor_cb_5:
                case R.id.repairDor_cb_6:{
                    //6个选项至少选中一个才视为信息完整
                    if(mCheckBox1.isChecked()||mCheckBox2.isChecked()||mCheckBox3.isChecked()||
                            mCheckBox4.isChecked()||mCheckBox5.isChecked()||mCheckBox6.isChecked()){
                        submitOrNot[0]=true;
                    }
                    else {
                        submitOrNot[0]=false;
                    }
                    //均完整，可提交
                    if(submitOrNot[0]&&submitOrNot[1]&&submitOrNot[2]){
                        mBtnSubmit.setEnabled(true);
                    }
                    else{
                        //不可提交
                        mBtnSubmit.setEnabled(false);
                    }
                    break;
                }
                case R.id.repairDor_submit:{
                    submitApply();
//                    //提交后直接退出页面
//                    Toast.makeText(repairDormitory.this,"提交成功",Toast.LENGTH_SHORT).show();
//                    repairDormitory.this.finish();
                    break;
                }
                default:break;
            }
        }
    }

    //进行判断当前是否有申请
    private void whetherSubmit(){
        //获取本地轻量数据库
        mUser=getSharedPreferences("userdata",MODE_PRIVATE);
        //获得本地数据的账号、密码
        String s_id=mUser.getString("s_id","");
        String password=mUser.getString("password","");
        //请求地址
        //http://39.97.114.188/Dormitory/servlet/IfCanFixApplyServelt?s_id=201830660178&password=123456
        String url = "http://39.97.114.188/Dormitory/servlet/IfCanFixApplyServelt?s_id="+s_id+"&password="+password;
        String tag = "whetherSubmit";
        //取得请求队列
        RequestQueue whetherSubmit = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        whetherSubmit.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest whetherSubmitrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response);
                            System.out.println("输出result："+jsonObject.getString("result"));
                            if(jsonObject.getString("result").equals("true")){
                                //返回值为true，代表有正在处理的申请，无法提交新的申请，result设为false
                                ifCanFixApply=false;
                                System.out.println("ifCanFixApply已被设置为："+ifCanFixApply);
                            }
                            else{
                                ifCanFixApply=true;
                                System.out.println("ifCanFixApply已被设置为："+ifCanFixApply);
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(repairDormitory.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(repairDormitory.this,"请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
        };
        //设置Tag标签
        whetherSubmitrequest.setTag(tag);
        //将请求添加到队列中
        whetherSubmit.add(whetherSubmitrequest);
    }

    //点击提交，将数据上传至接口
    private void submitApply(){
        if(!ifCanFixApply){
            Toast.makeText(this,"已有申请正在处理，无法再次提交",Toast.LENGTH_SHORT).show();
        }
        else{
            //学号
            String s_id=editStuNum.getHint().toString();
            //楼栋
//            String building=editBuilding.getHint().toString();
            //房间号
//            String room_num=editRoom.getHint().toString();
            //维修类别
            String maintenance="";
            if(mCheckBox1.isChecked()){maintenance+=" 电器";}
            if(mCheckBox2.isChecked()){maintenance+=" 桌椅";}
            if(mCheckBox3.isChecked()){maintenance+=" 空调";}
            if(mCheckBox4.isChecked()){maintenance+=" 建筑";}
            if(mCheckBox5.isChecked()){maintenance+=" 水设施";}
            if(mCheckBox6.isChecked()){maintenance+=" 其他";}
            //申请时间
            SimpleDateFormat dff = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
            dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
            String time = dff.format(new Date());
            //手机号
            String contact=mEdtPhoneNum.getText().toString();
            //备注
            String moreInformation=mEdtOther.getText().toString();

            //提交后直接退出页面，控制台输出提交信息
            System.out.println(s_id+"\n"+contact+"\n"+maintenance+"\n"+moreInformation+"\n"+time);
            //请求地址
            //http://39.97.114.188/Dormitory/servlet/MaintenanceApplyServlet?s_id=201830760178&maintenance=电器&remark=备注&contact=13000000&time=2018:06:12:12:30
            String url = "http://39.97.114.188/Dormitory/servlet/MaintenanceApplyServlet?s_id="+s_id+"&maintenance="+maintenance+"&remark="+moreInformation+"&contact="+contact+"&time="+time;
            String tag = "subRepairApply";
            //取得请求队列
            RequestQueue subRepairApply = Volley.newRequestQueue(this);
            //防止重复请求，所以先取消tag标识的请求队列
            subRepairApply.cancelAll(tag);
            //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
            final StringRequest subRepairApplyrequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                                if(jsonObject.getString("Result").equals("success")){
                                    //提交成功后直接退出页面
                                    Toast.makeText(repairDormitory.this,"提交成功",Toast.LENGTH_SHORT).show();
                                    repairDormitory.this.finish();
                                }
                                else{
                                    Toast.makeText(repairDormitory.this,"提交失败，请稍后重试！",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                                Toast.makeText(repairDormitory.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                    Toast.makeText(repairDormitory.this,"请稍后重试！",Toast.LENGTH_SHORT).show();
                }
            }) {
            };
            //设置Tag标签
            subRepairApplyrequest.setTag(tag);
            //将请求添加到队列中
            subRepairApply.add(subRepairApplyrequest);
        }
    }
}

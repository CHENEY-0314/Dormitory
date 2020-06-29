package com.example.dormitory.Student.MainPageActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dormitory.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class changeDormitory extends AppCompatActivity {
    //声明四个下拉框组件和图片组件
    private ImageView mBack;
    private Spinner department;
    private Spinner building;
    private CheckBox CheBox_floor_1,CheBox_floor_2,CheBox_floor_3,CheBox_floor_4,CheBox_floor_5,CheBox_floor_6,CheBox_floor_7,CheBox_floor_8;
    private CheckBox CheBox_bed_1,CheBox_bed_2,CheBox_bed_3,CheBox_bed_4,CheBox_bed_5;
    private Button BtnSubmit;
    private EditText editBuilding,editRoom;
    private LinearLayout Loading;//加载时显示
    private LinearLayout tabHead;//顶部导航栏和滚动式列表在加载时隐藏
    private ScrollView scrollArea;
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
                //打印选中的内容
                System.out.println(department.getSelectedItem());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        building.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //打印选中的内容
                System.out.println(building.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
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
        Loading=findViewById(R.id.gif_loading);
        tabHead=findViewById(R.id.changeDor_tab_head);
        scrollArea=findViewById(R.id.changeDor_scroll);
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

    //点击查看筛选结果，进行能否查看的判断，能则加载，不能则弹出提示
    private void ifCanChange(){
        //获取本地数据库中的账号密码
        mUser=getSharedPreferences("userdata",MODE_PRIVATE);
        String s_id=mUser.getString("s_id","");
        String password=mUser.getString("password","");
        //请求地址
        //http://39.97.114.188/Dormitory/servlet/ExchangingServlet?s_id=201830660178&password=123456
        String url = "http://39.97.114.188/Dormitory/servlet/ExchangingServlet?s_id="+s_id+"&password="+password;
        String tag = "ifCanChange";
        //取得请求队列
        RequestQueue ifCanChange = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        ifCanChange.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest ifCanChangerequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            System.out.println("输出JSONObject："+jsonObject);
                            if(jsonObject.getString("result").equals("success")){
                                AlertDialog.Builder builder=new AlertDialog.Builder(changeDormitory.this);
                                builder.setMessage("您有申请正在处理中，无法进行查看!").setCancelable(false).setPositiveButton("我知道了",null).show();

                            }
                            else{
                                lookSelectedResult();
                            }

                        } catch (JSONException e) {
                            System.out.println(e);
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(changeDormitory.this,"错误：JSONException",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(changeDormitory.this,"error",Toast.LENGTH_SHORT).show();
            }
        }) {
        };
        //设置Tag标签
        ifCanChangerequest.setTag(tag);
        //将请求添加到队列中
        ifCanChange.add(ifCanChangerequest);
    }
    //加载筛选结果
    private void lookSelectedResult(){
        Loading.setVisibility(View.VISIBLE);
        tabHead.setVisibility(View.GONE);
        scrollArea.setVisibility(View.GONE);
        //获取本人性别
        mUser=getSharedPreferences("userdata",MODE_PRIVATE);
        String sex=mUser.getString("sex","");
        //获取选中的楼栋、楼层、床位号
        //直接获取楼栋
        String Building=building.getSelectedItem().toString();
        //根据CheckBox选择情况获取楼层信息
        String Floor="";
        if(CheBox_floor_8.isChecked()){
            Floor+="0";
        }
        else{
            if(CheBox_floor_1.isChecked()) Floor+=(Floor.length()==0?"1":";1");
            if(CheBox_floor_2.isChecked()) Floor+=(Floor.length()==0?"2":";2");
            if(CheBox_floor_3.isChecked()) Floor+=(Floor.length()==0?"3":";3");
            if(CheBox_floor_4.isChecked()) Floor+=(Floor.length()==0?"4":";4");
            if(CheBox_floor_5.isChecked()) Floor+=(Floor.length()==0?"5":";5");
            if(CheBox_floor_6.isChecked()) Floor+=(Floor.length()==0?"6":";6");
            if(CheBox_floor_7.isChecked()) Floor+=(Floor.length()==0?"7":";7");
        }
        //根据CheckBox选择情况获取床位信息
        String Bed_num="";
        if(CheBox_bed_5.isChecked()){
            Bed_num+="0";
        }
        else{
            if(CheBox_bed_1.isChecked()) Bed_num+=(Bed_num.length()==0?"1":";1");
            if(CheBox_bed_2.isChecked()) Bed_num+=(Bed_num.length()==0?"2":";2");
            if(CheBox_bed_3.isChecked()) Bed_num+=(Bed_num.length()==0?"3":";3");
            if(CheBox_bed_4.isChecked()) Bed_num+=(Bed_num.length()==0?"4":";4");
        }
        //请求地址
        //
        String url = "http://39.97.114.188/Dormitory/servlet/ExchangeScreenServlet?sex="+sex+"&building="+Building+"&floor="+Floor+"&bed_num="+Bed_num;
        String tag = "SelectedResult";
        //取得请求队列
        RequestQueue SelectedResult = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        SelectedResult.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest SelectedResultrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response);
                            System.out.println("输出JSONObject："+jsonObject);
                            System.out.println(jsonObject.length());
                            if(jsonObject.length()!=0){
                                int n=jsonObject.length();
//                                ArrayList<String[]> selectedResult=new ArrayList<>();
                                String[] selectedResult=new String[n];
                                for(int i=1;i<=n;i++){
                                    JSONObject jsonObject1= (JSONObject) new JSONObject(response).get(String.valueOf(i));
                                    selectedResult[i-1]=jsonObject1.getString("building");
                                    selectedResult[i-1]+=";";
                                    selectedResult[i-1]+=jsonObject1.getString("room_num");
                                    selectedResult[i-1]+=";";
                                    selectedResult[i-1]+=jsonObject1.getString("bed_num");
                                    selectedResult[i-1]+=";";
                                    selectedResult[i-1]+=jsonObject1.getString("contact");
                                    selectedResult[i-1]+=";";
                                    selectedResult[i-1]+=jsonObject1.getString("target_id");
                                    selectedResult[i-1]+=";";
                                    selectedResult[i-1]+=jsonObject1.getString("name");
                                    selectedResult[i-1]+=";";
                                    selectedResult[i-1]+=jsonObject1.getString("sex");
                                }
//                                System.out.println(selectedResult[0]);
                                //跳转到筛选结果页面，将selectedResult传参过去
                                Intent intent=new Intent(changeDormitory.this,selectResult.class);
                                intent.putExtra("selectedResult",selectedResult);
                                startActivity(intent);

                                Loading.setVisibility(View.GONE);
                                tabHead.setVisibility(View.VISIBLE);
                                scrollArea.setVisibility(View.VISIBLE);
                            }
                            else{
                                Toast.makeText(changeDormitory.this,"无筛选结果",Toast.LENGTH_SHORT).show();
                                Loading.setVisibility(View.GONE);
                                tabHead.setVisibility(View.VISIBLE);
                                scrollArea.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            System.out.println(e);
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(changeDormitory.this,"错误：JSONException",Toast.LENGTH_SHORT).show();
                            Loading.setVisibility(View.GONE);
                            tabHead.setVisibility(View.VISIBLE);
                            scrollArea.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(changeDormitory.this,"error",Toast.LENGTH_SHORT).show();
//                Loading.setVisibility(View.GONE);
//                tabHead.setVisibility(View.VISIBLE);
//                scrollArea.setVisibility(View.VISIBLE);
            }
        }) {
        };
        //设置Tag标签
        SelectedResultrequest.setTag(tag);
        //将请求添加到队列中
        SelectedResult.add(SelectedResultrequest);
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
                    //进行判断，根据结果出现弹窗或者跳转到筛选结果页面
                    ifCanChange();
//                    lookSelectedResult();
//                    Intent intent=new Intent(changeDormitory.this,selectResult.class);
//                    startActivity(intent);
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


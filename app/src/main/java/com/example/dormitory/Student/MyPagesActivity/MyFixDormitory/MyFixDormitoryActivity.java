package com.example.dormitory.Student.MyPagesActivity.MyFixDormitory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dormitory.R;
import com.example.dormitory.Student.Adapters.TimeLineAdapter;
import com.example.dormitory.Student.MyPagesActivity.Trace;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MyFixDormitoryActivity extends AppCompatActivity {

    //声明控件
    private ListView lvTrace;
    private List<Trace> traceList = new ArrayList<>(10);
    private TimeLineAdapter adapter;
    private LinearLayout WithApply,NoApply;//整体界面的有无申请显示
    private LinearLayout Loading;//整体界面的加载显示
    private TextView txt_dormitory,txt_state,txt_ChangeApply,txt_DeleteApply;
    private SharedPreferences mUser;//用于获取本地数据库
    private SharedPreferences.Editor mUserEditor;//用于向本地数据库添加当前申请的fix_code
    private Boolean existApply;//当前是否存在报修申请
    private LinearLayout alterApply,verifyApply;//中间白色的“修改申请”栏和“确认验收”栏
    private Button btn_verify;//确认验收按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fix_dormitory);
        init();
    }

    //初始化函数，包括声明控件、绑定控件、设置点击监听器、查询是否有申请并为ifExistApply赋值
    private void init(){
        //点击上方返回按钮
        ImageView back = findViewById(R.id.AMFD_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFixDormitoryActivity.this.finish();
            }
        });

        //点击上方右侧三点返回按钮
        final ImageView more = findViewById(R.id.AMFD_more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();  //展开弹窗
            }
        });

        //点击修改申请
        txt_ChangeApply=findViewById(R.id.AMFD_changeApply);
        txt_ChangeApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改维修申请页面
                Intent intent = new Intent(MyFixDormitoryActivity.this, ChangeFixApplyActivity.class);
                MyFixDormitoryActivity.this.startActivity(intent);
            }
        });

        //点击撤销申请
        txt_DeleteApply=findViewById(R.id.AMFD_deleteApply);
        txt_DeleteApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //点击确认验收
        btn_verify=findViewById(R.id.AMFD_btn_verify);
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSureVerify();
            }
        });

        txt_dormitory=findViewById(R.id.AMFD_Dormitory);  //填写如 "C10-145 的维修单"
        txt_state=findViewById(R.id.AMFD_state);  // 未完成时显示 “处理中”  完成后显示 “已完成”
        mUser=getSharedPreferences("userdata",MODE_PRIVATE);//从本地数据库获得当前用户的楼栋号、房间号，并给相应的控件赋值
        txt_dormitory.setText(mUser.getString("building","XX")+"-"+mUser.getString("room_num","XX")+" 的维修单");
        txt_state.setText("处理中");

        WithApply=findViewById(R.id.AMFD_withapply);  //有申请时显示
        NoApply=findViewById(R.id.AMFD_NoApply);   //无申请时显示
        Loading=findViewById(R.id.gif_loading);//加载时显示

        lvTrace = (ListView)findViewById(R.id.AMFD_lvTrace);
        alterApply=findViewById(R.id.AMFD_alterApply);//有申请时并且处于状态1时显示
        verifyApply=findViewById(R.id.AMFD_verifyApply);//有申请时并且非处于状态1时显示

        //给existApply赋值,赋完值后直接执行加载list的函数
        set_existApply();
    }

    private void set_existApply(){
        //获取本地数据库
        mUser=getSharedPreferences("userdata",MODE_PRIVATE);
        //获取账号和密码
        String s_id=mUser.getString("s_id","");
        String password=mUser.getString("password","");
        //请求地址
        //http://39.97.114.188/Dormitory/servlet/IfCanFixApplyServelt?s_id=201830660178&password=123456
        String url = "http://39.97.114.188/Dormitory/servlet/IfCanFixApplyServelt?s_id="+s_id+"&password="+password;
        String tag="setApply";
        //取得请求队列
        RequestQueue setApply=Volley.newRequestQueue(this);
        //防止重复请求，先取消tag标识的全部请求
        setApply.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest setApplyRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=(JSONObject) new JSONObject(response);
                            System.out.println("输出result："+jsonObject.getString("result"));
                            if(jsonObject.getString("result").equals("true")){
                                //返回值为true，代表有正在处理的申请,existApply设为true
                                existApply=true;
                                System.out.println("existApply已被设置为："+existApply);
                                //根据existApply的值，执行加载list的操作
                                setListView();
                            }
                            else{
                                existApply=false;
                                System.out.println("existApply已被设置为："+existApply);
                                //根据existApply的值，执行加载list的操作
                                setListView();
                            }
                        }catch (JSONException e){
                            System.out.println(e);
                            Toast.makeText(MyFixDormitoryActivity.this,"JSONException:请稍后重试！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(MyFixDormitoryActivity.this,"error:无网络连接！",Toast.LENGTH_SHORT).show();
            }
        });
        //设置Tag标签
        setApplyRequest.setTag(tag);
        //将请求添加到队列中
        setApply.add(setApplyRequest);
    }

    //根据当前是否有申请，分别做不同的操作
    private void setListView(){
        //存在申请，则访问接口以获得相应数据
        if(existApply){
//            Loading.setVisibility(View.GONE);
//            WithApply.setVisibility(View.VISIBLE);
//            NoApply.setVisibility(View.GONE);
            //获取本地数据库
            mUser=getSharedPreferences("userdata",MODE_PRIVATE);
            mUserEditor=mUser.edit();
            //获取账号和密码
            String s_id=mUser.getString("s_id","");
            String password=mUser.getString("password","");
            //请求地址
            //http://39.97.114.188/Dormitory/servlet/GetFixApplyServlet?s_id=201830660178&password=123456
            String url = "http://39.97.114.188/Dormitory/servlet/GetFixApplyServlet?s_id="+s_id+"&password="+password;
            String tag="getApply";
            //取得请求队列
            RequestQueue getApply=Volley.newRequestQueue(this);
            //防止重复请求，先取消tag标识的全部请求
            getApply.cancelAll(tag);
            //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
            final StringRequest getApplyRequest=new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=(JSONObject) new JSONObject(response);
                                System.out.println(jsonObject);
                                if(jsonObject!=null){
                                    int n=jsonObject.length();
                                    if(n>=4){
                                        JSONObject jsonObject2=(JSONObject) new JSONObject(response).get("4");
                                        String time=jsonObject2.getString("time");
                                        StringBuilder time2=new StringBuilder(time);
                                        time2.replace(4,5,"-");
                                        time2.replace(7,8,"-");
                                        time2.replace(10,11," ");
                                        traceList.add(new Trace(time2.toString(), "维修完成!"));

                                    }
                                    if(n>=3){
                                        JSONObject jsonObject2=(JSONObject) new JSONObject(response).get("3");
                                        String time=jsonObject2.getString("time");
                                        StringBuilder time2=new StringBuilder(time);
                                        time2.replace(4,5,"-");
                                        time2.replace(7,8,"-");
                                        time2.replace(10,11," ");
                                        traceList.add(new Trace(time2.toString(), "维修成功，请确认验收。"));
                                    }
                                    if(n>=2){
                                        JSONObject jsonObject2=(JSONObject) new JSONObject(response).get("2");
                                        String time=jsonObject2.getString("time");
                                        StringBuilder time2=new StringBuilder(time);
                                        time2.replace(4,5,"-");
                                        time2.replace(7,8,"-");
                                        time2.replace(10,11," ");
                                        traceList.add(new Trace(time2.toString(), "等待宿舍维修。"));
                                    }
                                    if(n>=1){
                                        JSONObject jsonObject2=(JSONObject) new JSONObject(response).get("1");
                                        String time=jsonObject2.getString("time");
                                        StringBuilder time2=new StringBuilder(time);
                                        time2.replace(4,5,"-");
                                        time2.replace(7,8,"-");
                                        time2.replace(10,11," ");
                                        traceList.add(new Trace(time2.toString(), "您的申请已提交，将有管理员联系您，请留意您的电话。"));
                                        mUserEditor.putString("fix_code",jsonObject2.getString("fix_code"));
                                        mUserEditor.commit();
                                    }
                                    //设置适配器
                                    adapter = new TimeLineAdapter(MyFixDormitoryActivity.this, traceList);
                                    lvTrace.setAdapter(adapter);

                                    //设置上方和中间状态栏
                                    switch (n){
                                        case 1:{
                                            txt_state.setText("处理中");//处于状态1时修改上方状态，显示为“处理中”
                                            Loading.setVisibility(View.GONE);
                                            WithApply.setVisibility(View.VISIBLE);
                                            verifyApply.setVisibility(View.GONE);
                                            alterApply.setVisibility(View.VISIBLE);
                                            break;
                                        }
                                        case 2:{
                                            txt_state.setText("处理中");//处于状态2时修改上方状态，显示为“处理中”
                                            Loading.setVisibility(View.GONE);
                                            WithApply.setVisibility(View.VISIBLE);
                                            verifyApply.setVisibility(View.VISIBLE);
                                            alterApply.setVisibility(View.GONE);
                                            Button btn_verify=findViewById(R.id.AMFD_btn_verify);
                                            btn_verify.setEnabled(false);
                                            btn_verify.setText("确认验收");
                                            break;
                                        }
                                        case 3:{
                                            txt_state.setText("待验收");//处于状态3时修改上方状态，显示为“待验收”
                                            Loading.setVisibility(View.GONE);
                                            WithApply.setVisibility(View.VISIBLE);
                                            verifyApply.setVisibility(View.VISIBLE);
                                            alterApply.setVisibility(View.GONE);
                                            Button btn_verify=findViewById(R.id.AMFD_btn_verify);
                                            btn_verify.setEnabled(true);
                                            btn_verify.setText("确认验收");
                                            break;
                                        }
                                        case 4:
                                        case 5:{
                                            txt_state.setText("已完成");//维修完成时修改上方状态，显示为“已完成”
                                            Loading.setVisibility(View.GONE);
                                            WithApply.setVisibility(View.VISIBLE);
                                            verifyApply.setVisibility(View.VISIBLE);
                                            alterApply.setVisibility(View.GONE);
                                            Button btn_verify=findViewById(R.id.AMFD_btn_verify);
                                            btn_verify.setEnabled(false);
                                            btn_verify.setText("已验收");
                                            break;
                                        }
                                        default:{
                                            break;
                                        }
                                    }
                                }
                                else{
                                    System.out.println("json为空");
                                }
                            }catch (JSONException e){
                                System.out.println(e);
                                Toast.makeText(MyFixDormitoryActivity.this,"JSONException:请稍后重试！",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                    //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                    Toast.makeText(MyFixDormitoryActivity.this,"error:无网络连接！",Toast.LENGTH_SHORT).show();
                }
            });
            //设置Tag标签
            getApplyRequest.setTag(tag);
            //将请求添加到队列中
            getApply.add(getApplyRequest);


        }
        else {
            //--------------------------------若无申请，进行以下操作--------------------------------------
            Loading.setVisibility(View.GONE);
            WithApply.setVisibility(View.GONE);
            NoApply.setVisibility(View.VISIBLE);
        }
    }

    //确认验收按钮的点击函数
    private void makeSureVerify(){
        //获取本地数据库
        mUser=getSharedPreferences("userdata",MODE_PRIVATE);
        mUserEditor=mUser.edit();
        //获取账号和当前申请的fix_code
        String s_id=mUser.getString("s_id","");
        String fix_code=mUser.getString("fix_code","");
        //获取当前时间
        SimpleDateFormat dff = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String time = dff.format(new Date());
        //请求地址
        //http://39.97.114.188/Dormitory/servlet/CheckForFixServlet?fix_code=2819&s_id=201830660178&time=2020:06:22:10:07
        String url = "http://39.97.114.188/Dormitory/servlet/CheckForFixServlet?fix_code="+fix_code+"&s_id="+s_id+"&time="+time;
        String tag="makeSureVerify";
        //取得请求队列
        RequestQueue makeSureVerify=Volley.newRequestQueue(this);
        //防止重复请求，先取消tag标识的全部请求
        makeSureVerify.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest makeSureVerifyRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=(JSONObject) new JSONObject(response);
                            System.out.println("输出result："+jsonObject.getString("result"));
                            if(jsonObject.getString("result").equals("success")){
                                traceList.clear();
                                //根据existApply的值，执行加载list的操作
                                setListView();
                                //验证完成后删除本地的fix_code
                                mUserEditor.remove("fix_code");
                                mUserEditor.commit();
                            }
                            else{
                                Toast.makeText(MyFixDormitoryActivity.this,"状态3返回值：fail",Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            System.out.println(e);
                            Toast.makeText(MyFixDormitoryActivity.this,"JSONException:请稍后重试！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(MyFixDormitoryActivity.this,"error:无网络连接！",Toast.LENGTH_SHORT).show();
            }
        });
        //设置Tag标签
        makeSureVerifyRequest.setTag(tag);
        //将请求添加到队列中
        makeSureVerify.add(makeSureVerifyRequest);
    }

    //展开弹窗
    private void showBottomDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.dialog_myfixdor_more, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.bottom_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialog.findViewById(R.id.DMM_history_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查看历史记录
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.DMM_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消操作，收回弹窗
                dialog.dismiss();
            }
        });

    }


}

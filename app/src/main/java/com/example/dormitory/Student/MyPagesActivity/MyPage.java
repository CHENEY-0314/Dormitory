package com.example.dormitory.Student.MyPagesActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dormitory.Login.StuLogActivity;
import com.example.dormitory.R;
import com.example.dormitory.Student.MyPagesActivity.MyChangeDormitory.MyChangeDormitoryActivity;
import com.example.dormitory.Student.MyPagesActivity.MyFixDormitory.MyFixDormitoryActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPage extends Fragment {

    private CircleImageView Userimage;
    private TextView musername,mdormitory;
    private LinearLayout mChangeDormitory,mFixDormitory,mSetUp,mAdvice;

    private FrameLayout mframeLayout;
    //以下用于手机存用户信息
    private SharedPreferences mUser;
    private SharedPreferences.Editor mUserEditor;

    private View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.mypage_layout, container, false);
        init(); //声明控件，初始化控件
        updateInformation();
        GetTime();
        return mView;
    }
    //更新显示出的数据，例如楼栋、房间号等
    private void updateInformation(){
        //先更新本地数据库
        updateLocalDataBase();
        mUser=getActivity().getSharedPreferences("userdata",0);
        musername.setText(mUser.getString("s_name","未获取"));
        mdormitory.setText(mUser.getString("building","XX")+"-"+mUser.getString("room_num","XX")+"  "+mUser.getString("bed_num","X")+"号");

    }
    //更新本地数据库
    private void updateLocalDataBase(){
        //获取账号密码
        mUser=getActivity().getSharedPreferences("userdata",0);
        mUserEditor=mUser.edit();
        final String s_id=mUser.getString("s_id","");
        final String password=mUser.getString("password","");
        //请求地址
        String url = "http://39.97.114.188/Dormitory/servlet/GetStudentData?s_id="+s_id+"&password="+password;
        String tag = "Updata";
        //取得请求队列
        RequestQueue Updata = Volley.newRequestQueue(getActivity());
        //防止重复请求，所以先取消tag标识的请求队列
        Updata.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest Updatarequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //将从数据库获取到的消息保存在本地
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("结果");
                            mUserEditor.putString("s_id",jsonObject.getString("s_id"));
                            mUserEditor.putString("s_name",jsonObject.getString("s_name"));
                            mUserEditor.putString("sex",jsonObject.getString("sex"));
                            mUserEditor.putString("building",jsonObject.getString("building"));
                            mUserEditor.putString("room_num",jsonObject.getString("room_num"));
                            mUserEditor.putString("bed_num",jsonObject.getString("bed_num"));
                            mUserEditor.putString("phone_num",jsonObject.getString("contact"));
                            mUserEditor.putString("department",jsonObject.getString("college"));
                            mUserEditor.putString("password",password);
                            mUserEditor.apply();
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            System.out.println(e);
                            System.out.println("JSONException，请联系管理员！");
                            Toast.makeText(getActivity(),"更新失败！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                System.out.println(error);
                System.out.println("VolleyError，请联系管理员！");
                Toast.makeText(getActivity(),"无网络连接,请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("s_id", s_id);
                params.put("password", password);
                return params;
            }
        };
        //设置Tag标签
        Updatarequest.setTag(tag);
        //将请求添加到队列中
        Updata.add(Updatarequest);
    }

    //声明控件，初始化控件
    private void init(){
        //声明控件
        Userimage=mView.findViewById(R.id.MyPage_userimg);
        musername= mView.findViewById(R.id.MyPage_txt_username);
        mdormitory= mView.findViewById(R.id.MyPage_txt_home);
        mChangeDormitory= mView.findViewById(R.id.MyPage_btn_ChangeD);
        mFixDormitory= mView.findViewById(R.id.MyPage_btn_FixD);
        mSetUp= mView.findViewById(R.id.MyPage_btn_setup);
        mAdvice= mView.findViewById(R.id.MyPage_btn_advice);
        mframeLayout=mView.findViewById(R.id.background3);
        //声明点击事件
        Userimage.setOnClickListener(new ButtonListener());
        mChangeDormitory.setOnClickListener(new ButtonListener());
        mFixDormitory.setOnClickListener(new ButtonListener());
        mSetUp.setOnClickListener(new ButtonListener());
        mAdvice.setOnClickListener(new ButtonListener());
    }

    //处理点击事件
    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.MyPage_userimg:  //跳转到更换头像页面
                    Userimage.setClickable(false);
                    Intent intent = new Intent(getActivity(), ChangeUserImgActivity.class);
                    startActivity(intent);
                    Userimage.setClickable(true);
                    break;
                case R.id.MyPage_btn_ChangeD: //跳转到我的更换宿舍申请页面
                    mChangeDormitory.setClickable(false);
                    Intent intent2 = new Intent(getActivity(), MyChangeDormitoryActivity.class);
                    startActivity(intent2);
                    mChangeDormitory.setClickable(true);
                    break;
                case R.id.MyPage_btn_FixD:  //跳转到我的维修申请页面
                    mFixDormitory.setClickable(false);
                    Intent intent3 = new Intent(getActivity(), MyFixDormitoryActivity.class);
                    startActivity(intent3);
                    mFixDormitory.setClickable(true);
                    break;
                case R.id.MyPage_btn_setup:  //跳转到设置页面
                    mSetUp.setClickable(false);
                    Intent intent4 = new Intent(getActivity(), SetupActivity.class);
                    startActivity(intent4);
                    mSetUp.setClickable(true);
                    break;
                case R.id.MyPage_btn_advice:
                    mAdvice.setClickable(false);
                    Intent intent5 = new Intent(getActivity(), AdviceActivity.class);
                    startActivity(intent5);
                    mAdvice.setClickable(true);
                    break;
                default:break;
            }
        }
    }

    private void GetTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");//获取时间的小时数
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        if(simpleDateFormat.format(date).compareTo("18")>0||simpleDateFormat.format(date).compareTo("06")<0){
            mframeLayout.setBackgroundResource(R.drawable.background3);
        }
    }

    }

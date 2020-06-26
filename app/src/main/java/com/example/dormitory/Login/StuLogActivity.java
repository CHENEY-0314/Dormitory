package com.example.dormitory.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dormitory.R;
import com.example.dormitory.Student.MainPageActivity.MainPage;
import com.example.dormitory.WelcomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class StuLogActivity extends AppCompatActivity {
    //用户名和密码控件
    private EditText textAccount,textPassword;
    //以下用于手机存用户信息
    private SharedPreferences mUser;
    private SharedPreferences.Editor mUserEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_log);
        //绑定账户和密码控件
        textAccount=findViewById(R.id.stu_account);
        textPassword=findViewById(R.id.stu_password);

        mUser=getSharedPreferences("userdata",MODE_PRIVATE);
        mUserEditor=mUser.edit();


        Button mBtnSLogin = findViewById(R.id.btn_sLogin);
        mBtnSLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginVerify();
                //UpdateUserdate(sAccount.getText().toString(),sPassword.getText().toString());
//                Intent intent = new Intent(StuLogActivity.this, WelcomeActivity.class);
//                startActivity(intent);
            }
        });
    }
    //登录验证
    public void loginVerify(){
        //获得输入的账号、密码
        final String s_id=textAccount.getText().toString();
        final String password=textPassword.getText().toString();
        //请求地址
        String url="http://39.97.114.188/Dormitory/servlet/StuLoginServlet?s_id="+s_id+"&password="+password;
        String tag= "login";
        //取得请求队列
        RequestQueue login = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        login.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest loginrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            String tempResult=jsonObject.getString("Result").toString();
                            System.out.println(jsonObject.getString("Result").toString());
                            System.out.println("tempResult"+tempResult);
                            if(tempResult.equals("success")){
                                //账号密码正确则执行登录与本地数据库信息更新操作
                                UpdateUserdate(s_id,password);
                            }
                            else{
                                Toast.makeText(StuLogActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            System.out.print(e);
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(StuLogActivity.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(StuLogActivity.this,"请稍后重试！",Toast.LENGTH_SHORT).show();
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
        loginrequest.setTag(tag);
        //将请求添加到队列中
        login.add(loginrequest);
    }

    //登陆时更新当前登录的用户信息
    public void UpdateUserdate(final String s_id, final String password){
        //请求地址
        String url = "http://39.97.114.188/Dormitory/servlet/GetStudentData?s_id="+s_id+"&password="+password;
        String tag = "Updata";
        //取得请求队列
        RequestQueue Updata = Volley.newRequestQueue(this);
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
                            //显示“欢迎你：XXX”的toast弹窗
                            Toast toast=Toast.makeText(StuLogActivity.this,null,Toast.LENGTH_SHORT);
                            toast.setText("欢迎你:"+jsonObject.getString("s_name"));
                            toast.show();
                            //下面做了一个延迟1800ms跳转的效果
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(StuLogActivity.this, WelcomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);  //设置跳转动画
                                }
                            },1800);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(StuLogActivity.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(StuLogActivity.this,"请稍后重试！",Toast.LENGTH_SHORT).show();
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


}

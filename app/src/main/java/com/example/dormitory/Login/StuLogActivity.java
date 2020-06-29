package com.example.dormitory.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.transition.Slide;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import pl.droidsonroids.gif.GifImageView;


public class StuLogActivity extends AppCompatActivity {
    //用户名和密码控件
    private EditText textAccount,textPassword;
    //以下用于手机存用户信息
    private SharedPreferences mUser;
    private SharedPreferences.Editor mUserEditor;

    private ImageView back;
    private Button btnlogin;

    private GifImageView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setEnterTransition(new Slide().setDuration(950));  //设置退场动画

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_log);
        //绑定账户和密码控件
        textAccount=findViewById(R.id.stu_account);
        textPassword=findViewById(R.id.stu_password);
        loading=findViewById(R.id.sLogin_loading);

        mUser=getSharedPreferences("userdata",MODE_PRIVATE);
        mUserEditor=mUser.edit();


        btnlogin= findViewById(R.id.btn_sLogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnlogin.setEnabled(false);
                ifEmpty(); //判断两个输入框是否为空
            }
        });

        back=findViewById(R.id.stulogin_back);  //点击上方返回按钮
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StuLogActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                overridePendingTransition(0,R.anim.fade_out);
            }
        });

    }

    //判断输入框是否为空
    public  void ifEmpty(){
        //获得输入的账号、密码
        final String s_id=textAccount.getText().toString();
        final String password=textPassword.getText().toString();

        if(s_id .equals("")||s_id.length()!=12){
            textAccount.setError("请正确输入");
            btnlogin.setEnabled(true);
        }else if(password.equals("")){
            textPassword.setError("请正确输入");
            btnlogin.setEnabled(true);
        }else {
            btnlogin.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
            loginVerify(s_id,password);  //都不为空则进行登录
        }

    }

    //登录验证
    public void loginVerify(final String s_id, final String password){

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
                            if(tempResult.equals("success")){
                                //账号密码正确则执行登录与本地数据库信息更新操作
                                UpdateUserdate(s_id,password);
                            }
                            else{
                                Toast.makeText(StuLogActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                                btnlogin.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.GONE);
                                btnlogin.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            System.out.print(e);
                            Toast.makeText(StuLogActivity.this,"登录异常，请联系管理员！",Toast.LENGTH_SHORT).show();
                            btnlogin.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            btnlogin.setEnabled(true);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(StuLogActivity.this,"无网络连接,请稍后重试！",Toast.LENGTH_SHORT).show();
                btnlogin.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                btnlogin.setEnabled(true);
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
                            mUserEditor.putString("firstopentime","2020:06:18:00:00");
                            mUserEditor.apply();
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Updateintention(s_id);
                                }
                            },800);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            System.out.println(e);
                            System.out.println("JSONException，请联系管理员！");
                            Toast.makeText(StuLogActivity.this,"登录异常，请联系管理员！",Toast.LENGTH_SHORT).show();
                            btnlogin.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            btnlogin.setEnabled(true);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                System.out.println(error);
                System.out.println("VolleyError，请联系管理员！");
                Toast.makeText(StuLogActivity.this,"无网络连接,请稍后重试！",Toast.LENGTH_SHORT).show();
                btnlogin.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                btnlogin.setEnabled(true);
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

    //登陆时更新当前登录的用户信息
    public void Updateintention(final String s_id){
        //请求地址
        String url = "http://39.97.114.188/Dormitory/servlet/IfHaveIntentionServlet?s_id="+s_id;
        String tag = "check";
        //取得请求队列
        RequestQueue check = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        check.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest checkrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //将从数据库获取到的消息保存在本地
                            JSONObject jsonObject = (JSONObject) new JSONObject(response);
                            if(jsonObject.getString("result").equals("false")){
                                mUserEditor.putBoolean("switch_state",false);
                                mUserEditor.apply();
                            }else {
                                mUserEditor.putBoolean("switch_state",true);
                                mUserEditor.apply();
                                Toast.makeText(StuLogActivity.this,"true",Toast.LENGTH_SHORT).show();
                            }
                            //显示“欢迎你：XXX”的toast弹窗
                            Toast toast=Toast.makeText(StuLogActivity.this,null,Toast.LENGTH_SHORT);
                            toast.setText("欢迎你:"+mUser.getString("s_name",""));
                            toast.show();
                            //下面做了一个延迟1800ms跳转的效果
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(StuLogActivity.this, WelcomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);  //设置跳转动画
                                }
                            },1800);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            System.out.println(e);
                            System.out.println("登录异常，请联系管理员！");
                            Toast.makeText(StuLogActivity.this,"登录异常，请联系管理员！",Toast.LENGTH_SHORT).show();
                            btnlogin.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            btnlogin.setEnabled(true);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(StuLogActivity.this,"无网络连接,请稍后重试！",Toast.LENGTH_SHORT).show();
                btnlogin.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                btnlogin.setEnabled(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("s_id", s_id);
                return params;
            }
        };
        //设置Tag标签
        checkrequest.setTag(tag);
        //将请求添加到队列中
        check.add(checkrequest);
    }


    //重写返回函数
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            startActivity(new Intent(StuLogActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            overridePendingTransition(0,R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    //点击空白处收起键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
                textAccount.clearFocus();
                textPassword.clearFocus();
            }
        }
        return super.dispatchTouchEvent(me);
    }
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }
    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}

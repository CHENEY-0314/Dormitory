package com.example.dormitory.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.transition.Slide;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dormitory.Administrator.AdmActivity;
import com.example.dormitory.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class AdmLogActivity extends AppCompatActivity {
    //声明3个控件，账号 密码 登录
    private EditText editAccount,editPassword;
    private Button mBtnALogin;

    private ImageView back;
    private GifImageView loading;

    //以下用于手机存管理员信息
    private SharedPreferences mAdm;
    private SharedPreferences.Editor mAdmEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setEnterTransition(new Slide().setDuration(950));  //设置退场动画

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_log);

        //绑定控件
        editAccount = findViewById(R.id.adm_account);
        editPassword = findViewById(R.id.adm_password);
        mBtnALogin = findViewById(R.id.btn_aLogin);
        loading=findViewById(R.id.aLogin_loading);

        //建立轻量数据库
        mAdm=getSharedPreferences("admdata",MODE_PRIVATE);
        mAdmEditor=mAdm.edit();

        //点击登录按钮，调用loginVerify函数进行登录验证
        mBtnALogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnALogin.setEnabled(false);
                ifEmpty();  //判断是否为空，如果输入框不为空则执行登录
            }
        });

        back=findViewById(R.id.admlogin_back);  //点击上方返回按钮
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdmLogActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                overridePendingTransition(0,R.anim.fade_out);
            }
        });
    }

    //判断输入框是否为空
    public  void ifEmpty(){
        //获得输入的账号、密码
        final String s_id=editAccount.getText().toString();
        final String password=editPassword.getText().toString();

        if(s_id .equals("")){
            editAccount.setError("请正确输入");
            mBtnALogin.setEnabled(true);
        }else if(password.equals("")){
            editPassword.setError("请正确输入");
            mBtnALogin.setEnabled(true);
        }else {
            mBtnALogin.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
            loginVerify(s_id,password);  //都不为空则进行登录
        }

    }

    //登录验证函数
    private void loginVerify(final String a_id, final String password){
        //请求地址
        //http://39.97.114.188/Dormitory/servlet/AdmLoginServlet?a_id=000001&password=123456
        String url="http://39.97.114.188/Dormitory/servlet/AdmLoginServlet?a_id="+a_id+"&password="+password;
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
                            //用一个字符串接受返回的成功与否数据，下面三行只是为了能够在控制台看到效果，可以在if里面直接用jsonObject.getString("Result")进行验证
                            String tempResult=jsonObject.getString("Result").toString();
                            System.out.println(jsonObject.getString("Result").toString());
                            System.out.println("tempResult"+tempResult);
                            if(tempResult.equals("success")){
                                mAdmEditor.putString("a_id",a_id);
                                mAdmEditor.putString("password",password);
                                mAdmEditor.apply();
                                startActivity(new Intent(AdmLogActivity.this, AdmActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                overridePendingTransition(0,R.anim.fade_out);
                                Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"账号或密码错误",Toast.LENGTH_SHORT).show();
                                mBtnALogin.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.GONE);
                                mBtnALogin.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            System.out.print(e);
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(getApplicationContext(),"登录异常，请联系管理员！",Toast.LENGTH_SHORT).show();
                            mBtnALogin.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            mBtnALogin.setEnabled(true);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getApplicationContext(),"无网络连接,请稍后重试！",Toast.LENGTH_SHORT).show();
                mBtnALogin.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                mBtnALogin.setEnabled(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("a_id", a_id);
                params.put("password", password);
                return params;
            }
        };
        //设置Tag标签
        loginrequest.setTag(tag);
        //将请求添加到队列中
        login.add(loginrequest);
    }


    //重写返回函数
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            startActivity(new Intent(AdmLogActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
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
                editAccount.clearFocus();
                editPassword.clearFocus();
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

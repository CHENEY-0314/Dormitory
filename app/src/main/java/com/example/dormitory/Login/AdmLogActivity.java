package com.example.dormitory.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class AdmLogActivity extends AppCompatActivity {
    //声明3个控件，账号 密码 登录
    private EditText editAccount,editPassword;
    private Button mBtnALogin;

    //以下用于手机存管理员信息
    private SharedPreferences mAdm;
    private SharedPreferences.Editor mAdmEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_log);

        //绑定控件
        editAccount = findViewById(R.id.adm_account);
        editPassword = findViewById(R.id.adm_password);
        mBtnALogin = findViewById(R.id.btn_aLogin);

        //建立轻量数据库
        mAdm=getSharedPreferences("admdata",MODE_PRIVATE);
        mAdmEditor=mAdm.edit();

        //点击登录按钮，调用loginVerify函数进行登录验证
        mBtnALogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginVerify();
            }
        });
    }

    //登录验证函数
    private void loginVerify(){
        //获得输入的账号、密码
        final String a_id=editAccount.getText().toString();
        final String password=editPassword.getText().toString();
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
//                                Intent intent = new Intent(AdmLogActivity.this, AdmActivity.class);
//                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"账号或密码错误",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            System.out.print(e);
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(getApplicationContext(),"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getApplicationContext(),"请稍后重试！",Toast.LENGTH_SHORT).show();
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
}

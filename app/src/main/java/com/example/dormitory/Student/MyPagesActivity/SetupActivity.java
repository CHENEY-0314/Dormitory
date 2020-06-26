package com.example.dormitory.Student.MyPagesActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dormitory.Administrator.AdmActivity;
import com.example.dormitory.Login.AdmLogActivity;
import com.example.dormitory.Login.LoginActivity;
import com.example.dormitory.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SetupActivity extends AppCompatActivity {

    private ImageView mBack,mEditPhoneNum;
    private TextView mPhoneNum;
    private LinearLayout mloginOut;
    private LinearLayout aboutUs;

    //以下用于手机存用户信息
    private SharedPreferences mUser;
    private SharedPreferences.Editor mUserEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mBack=findViewById(R.id.Setup_back);
        mEditPhoneNum=findViewById(R.id.Setup_editPhoneNum);
        mPhoneNum=findViewById(R.id.Setup_phoneNum);
        mloginOut=findViewById(R.id.Setup_exitlogin);
        aboutUs=findViewById(R.id.Setup_aboutus);

        //点击上方返回按钮
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetupActivity.this.finish();
            }
        });
        //获得本地轻量数据库
        mUser=getSharedPreferences("userdata",0);
        mUserEditor=mUser.edit();
        //从本地获取手机号并显示
        mPhoneNum.setText(mUser.getString("phone_num","未获取"));
        //点击修改手机号图标
        mEditPhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //建一个弹窗
                final AlertDialog.Builder builder=new AlertDialog.Builder(SetupActivity.this);
                //获得一个自定义布局
                View view= LayoutInflater.from(SetupActivity.this).inflate(R.layout.dialog_mypage_edit_phone,null);
                //绑定自定义布局里的EditText控件
                final EditText editPhone=view.findViewById(R.id.edit_phone);
                //将自定义布局布置到弹窗里，并设置弹窗外不可点击
                builder.setView(view);
                //设置确认按钮
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String phoneNum=editPhone.getText().toString();
                        //手机号位数为11，可以修改
                        if(phoneNum.length()==11){
                            updatePhoneNum(phoneNum);
                        }
                        //手机号位数不对
                        else if(phoneNum.length()!=0){
                            Toast.makeText(SetupActivity.this,"手机号位数错误",Toast.LENGTH_SHORT).show();
                        }
                        //未输入手机号
                        else {
                            Toast.makeText(SetupActivity.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //设置取消按钮
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
                builder.show();
            }
        });

        mloginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击账号注销");
                final AlertDialog.Builder builder_loginOut=new AlertDialog.Builder(SetupActivity.this);
                builder_loginOut.setTitle("是否注销账号").setMessage("退出登录后需重新登录并加载数据");
                builder_loginOut.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mUserEditor.clear();
                        mUserEditor.apply();
                        Intent intent = new Intent(SetupActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                builder_loginOut.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
                builder_loginOut.show();
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }
    //更改联系方式
    private void updatePhoneNum(final String phoneNum){
        //获取账号和密码
        String s_id=mUser.getString("s_id","");
        String password=mUser.getString("password","");
        //请求地址
        //http://39.97.114.188/Dormitory/servlet/ChangeStuContactServlet?s_id=201830660178&password=123456&number=18818273621
        String url = "http://39.97.114.188/Dormitory/servlet/ChangeStuContactServlet?s_id="+s_id+"&password="+password+"&number="+phoneNum;
        String tag="updatePhone";
        //取得请求队列
        RequestQueue updatePhone=Volley.newRequestQueue(this);
        //防止重复请求，先取消tag标识的全部请求
        updatePhone.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest updatePhoneRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=(JSONObject) new JSONObject(response);
                            System.out.println("输出result："+jsonObject.getString("result"));
                            if(jsonObject.getString("result").equals("success")){
                                //更新本地数据库中的手机号
                                mUserEditor.putString("phone_num",phoneNum);
                                mUserEditor.commit();
                                //更新控件显示的手机号
                                mPhoneNum.setText(phoneNum);
                                Toast.makeText(SetupActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(SetupActivity.this,"上传失败！",Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            System.out.println(e);
                            Toast.makeText(SetupActivity.this,"JSONException:请稍后重试！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(SetupActivity.this,"error:请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        });
        //设置Tag标签
        updatePhoneRequest.setTag(tag);
        //将请求添加到队列中
        updatePhone.add(updatePhoneRequest);
    }


}

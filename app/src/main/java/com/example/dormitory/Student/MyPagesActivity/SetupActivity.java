package com.example.dormitory.Student.MyPagesActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dormitory.Login.LoginActivity;
import com.example.dormitory.R;

public class SetupActivity extends AppCompatActivity {

    private ImageView mBack,mEditPhoneNum;
    private TextView mPhoneNum;
    private LinearLayout mloginOut;

    //以下用于手机存用户信息
    private SharedPreferences mUser;
    private SharedPreferences.Editor mUserEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mBack=findViewById(R.id.Setup_back);
        mEditPhoneNum=findViewById(R.id.Setup_editPhoneNum);
        mPhoneNum=findViewById(R.id.Setup_phoneNum);
        mloginOut=findViewById(R.id.Setup_exitlogin);

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
                builder.setView(view).setCancelable(false);
                //设置确认按钮
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //手机号位数为11，可以修改
                        if(editPhone.getText().toString().length()==11){
                            mPhoneNum.setText(editPhone.getText().toString());
                        }
                        //手机号位数不对
                        else if(editPhone.getText().toString().length()!=0){
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
                builder_loginOut.setTitle("是否注销账号").setMessage("退出登录后需重新登录并加载数据").setCancelable(false);
                builder_loginOut.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mUserEditor.clear();
                        mUserEditor.commit();
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

    }

}

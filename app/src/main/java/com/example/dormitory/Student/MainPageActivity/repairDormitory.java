package com.example.dormitory.Student.MainPageActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dormitory.R;

public class repairDormitory extends AppCompatActivity {
    private ImageView mBack;//声明返回按钮的组件
    private TextView mOtherNum;//备注处字数组件
    private EditText mEdtOther,mEdtPhoneNum;//声明备注和联系方式组件
    private CheckBox mCheckBox1,mCheckBox2,mCheckBox3,mCheckBox4,mCheckBox5,mCheckBox6;//声明报修处6个组件
    private Button mBtnSubmit;//声明提交按钮组件
    private Boolean[] submitOrNot;//用于判断能否提交
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_dormitory);
        //初始化函数包括绑定控件、声明监听事件、返回事件、初始化submitOrNot布尔数组
        init();
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
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
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
                    //提交后直接退出页面
                    Toast.makeText(repairDormitory.this,"提交成功",Toast.LENGTH_SHORT).show();
                    repairDormitory.this.finish();
                    break;
                }
                default:break;
            }
        }
    }
}

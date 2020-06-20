package com.example.dormitory.Student.MyPagesActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dormitory.R;
import com.example.dormitory.Student.MainPageActivity.repairDormitory;

import org.json.JSONException;
import org.json.JSONObject;

public class AdviceActivity extends AppCompatActivity {

    private EditText adtAdvice;
    private Button mbtnsubmit;
    private TextView mTextNum;
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);

        adtAdvice=findViewById(R.id.Advice_edt);
        mbtnsubmit=findViewById(R.id.Advice_btn);
        mTextNum=findViewById(R.id.Advice_num);
        mBack=findViewById(R.id.Advice_back);

        //监听输入框字数变化
        adtAdvice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {  //监听输入框字数变化
                mTextNum.setText(String.valueOf(s.length())+"/600");
                if(s.length()>=10){
                    mbtnsubmit.setEnabled(true);
                }else mbtnsubmit.setEnabled(false);
            }
        });

        //点击提交按钮
        mbtnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //上传数据
                submitAdvice();
            }
        });

        //点击上方返回按钮
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdviceActivity.this.finish();
            }
        });

    }

    //点击提交后，将数据上传
    private void submitAdvice(){
        final String content=adtAdvice.getText().toString();
        //请求地址
        //http://39.97.114.188/Dormitory/servlet/FeedbackServlet?content=这是意见反馈的内容
        String url = "http://39.97.114.188/Dormitory/servlet/FeedbackServlet?content="+content;
        String tag = "subAdvice";
        //取得请求队列
        RequestQueue subAdvice = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        subAdvice.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest subAdvicerequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            System.out.println(jsonObject.getString("Result"));
                            System.out.println("success: "+content.length());
                            if(jsonObject.getString("Result").equals("success: "+content.length())){
                                //提交成功后直接退出页面
                                Toast.makeText(AdviceActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                                AdviceActivity.this.finish();
                            }
                            else{
                                Toast.makeText(AdviceActivity.this,"提交失败，请稍后重试！",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(AdviceActivity.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(AdviceActivity.this,"请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
        };
        //设置Tag标签
        subAdvicerequest.setTag(tag);
        //将请求添加到队列中
        subAdvice.add(subAdvicerequest);
    }



}

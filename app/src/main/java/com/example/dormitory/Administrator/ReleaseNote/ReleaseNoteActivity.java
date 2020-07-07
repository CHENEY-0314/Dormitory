package com.example.dormitory.Administrator.ReleaseNote;

import android.app.Dialog;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dormitory.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ReleaseNoteActivity extends AppCompatActivity {
    private EditText head;
    private EditText note;
    private TextView notenum;
    private Button mbtnsubmit;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_note);

        head=findViewById(R.id.admNoteHeadEdt);
        back=findViewById(R.id.ReleaseNote_back);
        note=findViewById(R.id.admNoteEdt);
        notenum=findViewById(R.id.Note_num);
        mbtnsubmit=findViewById(R.id.admNoteSubmit_btn);

        //监听输入框字数变化
        note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {  //监听输入框字数变化
                notenum.setText(String.valueOf(s.length())+"/2000");
                if(s.length()>30&&head.getText().toString()!=null){
                    mbtnsubmit.setEnabled(true);
                }else {mbtnsubmit.setEnabled(false);
                if(s.length()>30)
                    Toast.makeText(ReleaseNoteActivity.this,"字数不足",Toast.LENGTH_SHORT).show();
                if(head.getText().toString()==null)
                    Toast.makeText(ReleaseNoteActivity.this,"请输入标题",Toast.LENGTH_SHORT).show();
                }
            }
        });


        mbtnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击提交通知按钮
                final Dialog dlg = new Dialog(ReleaseNoteActivity.this);
                dlg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dlg.show();
                Window window = dlg.getWindow();
                WindowManager windowManager = ReleaseNoteActivity.this.getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
                Point size =new Point();
                display.getSize(size);
                int width= size.x;
                lp.width = (int) (width-200); // 设置宽度
                dlg.getWindow().setAttributes(lp);
                window.setContentView(R.layout.dialog_box_adm);
                // 为确认按钮添加事件,执行退出应用操作
                Button btnok=(Button)window.findViewById(R.id.btn_ok);
                Button btncancel=(Button)window.findViewById(R.id.btn_cancel);
                TextView text=(TextView)window.findViewById(R.id.dialog_box_text);
                text.setText("确认发布？");
                btnok.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String headtext=head.getText().toString();
                        String content=note.getText().toString();
                        sendData(headtext,content);
                        mbtnsubmit.setText("发布中…");
                        dlg.dismiss();
                    }
                });
                btncancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dlg.cancel();
                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {  //点击上方返回按钮
            @Override
            public void onClick(View v) {
                ReleaseNoteActivity.this.finish();
            }
        });

    }
    public void sendData(String head,String content){
        String time=setTime();
        //请求地址
        String url = "http://39.97.114.188/Dormitory/servlet/ReleaseNoteServlet?head="+head+"&content="+content+"&time="+time;
        String tag = "ReleaseData";
        //取得请求队列
        RequestQueue ReleaseData = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        ReleaseData.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest ReleaseDatarequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            String Result=jsonObject.getString("Result").toString();
                            System.out.println(Result);
                            if(Result.equals("Success")){
                                System.out.println(Result);
                                mbtnsubmit.setText("发布");
                                mbtnsubmit.setEnabled(false);
                                finish();
                                Toast toast=Toast.makeText(ReleaseNoteActivity.this,null,Toast.LENGTH_SHORT);
                                toast.setText("发布成功");
                                toast.show();
                            }
                        } catch (JSONException e) {

                            Toast.makeText(ReleaseNoteActivity.this,"发布失败",Toast.LENGTH_SHORT).show();
                            mbtnsubmit.setText("发布");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(ReleaseNoteActivity.this,"未发布",Toast.LENGTH_SHORT).show();
                mbtnsubmit.setText("发布");
            }
        }) {

        };
        //设置Tag标签
        ReleaseDatarequest.setTag(tag);
        //将请求添加到队列中
        ReleaseData.add(ReleaseDatarequest);
    }
    public String setTime() {
        SimpleDateFormat dff = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String time = dff.format(new Date());
        return time;
    }
}

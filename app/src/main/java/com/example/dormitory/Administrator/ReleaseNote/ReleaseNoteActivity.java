package com.example.dormitory.Administrator.ReleaseNote;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dormitory.R;

public class ReleaseNoteActivity extends AppCompatActivity {
    private EditText head;
    private EditText note;
    private TextView notenum;
    private Button mbtnsubmit;
    private ImageView back;
    SendReleaseNoteData sendReleaseNoteData;
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
                String headtext=head.getText().toString();
                String content=note.getText().toString();
                sendReleaseNoteData=new SendReleaseNoteData(headtext,content);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {  //点击上方返回按钮
            @Override
            public void onClick(View v) {
                ReleaseNoteActivity.this.finish();
            }
        });

    }
}

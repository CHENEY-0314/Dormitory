package com.example.dormitory.Student.MyPagesActivity.MyFixDormitory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dormitory.R;
import com.example.dormitory.Student.Adapters.TimeLineAdapter;
import com.example.dormitory.Student.MyPagesActivity.Trace;

import java.util.ArrayList;
import java.util.List;

public class MyFixDormitoryActivity extends AppCompatActivity {

    //声明控件
    private ListView lvTrace;
    private List<Trace> traceList = new ArrayList<>(10);
    private TimeLineAdapter adapter;
    private LinearLayout WithApply,NoApply;
    private TextView txt_dormitory,txt_state,txt_ChangeApply,txt_DeleteApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fix_dormitory);

        //点击上方返回按钮
        ImageView back = findViewById(R.id.AMFD_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFixDormitoryActivity.this.finish();
            }
        });

        //点击上方右侧三点返回按钮
        final ImageView more = findViewById(R.id.AMFD_more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();  //展开弹窗
            }
        });

        //点击修改申请
        txt_ChangeApply=findViewById(R.id.AMFD_changeApply);
        txt_ChangeApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改维修申请页面
                Intent intent = new Intent(MyFixDormitoryActivity.this, ChangeFixApplyActivity.class);
                MyFixDormitoryActivity.this.startActivity(intent);
            }
        });

        //点击撤销申请
        txt_DeleteApply=findViewById(R.id.AMFD_deleteApply);
        txt_DeleteApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        txt_dormitory=findViewById(R.id.AMFD_Dormitory);  //填写如 "C10-145 的维修单"
        txt_state=findViewById(R.id.AMFD_state);  // 未完成时显示 “处理中”  完成后显示 “已完成”


        WithApply=findViewById(R.id.AMFD_withapply);  //有申请时显示
        NoApply=findViewById(R.id.AMFD_NoApply);   //无申请时显示


        //判断当前是否有申请-------------若有则进行以下操作---------------------------------------------

        lvTrace = (ListView)findViewById(R.id.AMFD_lvTrace);

        // 向List中添加数据，数据包括时间以及状态，如下所示
        traceList.add(new Trace("2020-06-10 14:13:00", "维修完成!"));
        traceList.add(new Trace("2020-05-27 13:01:04", "维修成功，请确认验收。"));
        traceList.add(new Trace("2020-05-25 12:19:47", "等待宿舍维修。"));
        traceList.add(new Trace("2020-05-20 11:12:44", "您的申请已提交，将有管理员联系您，请留意您的电话。"));

        //设置适配器
        adapter = new TimeLineAdapter(this, traceList);
        lvTrace.setAdapter(adapter);


        //--------------------------------若无申请，进行以下操作--------------------------------------
        //WithApply.setVisibility(View.GONE);
        //NoApply.setVisibility(View.VISIBLE);

    }

    //展开弹窗
    private void showBottomDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.dialog_myfixdor_more, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.bottom_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialog.findViewById(R.id.DMM_history_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查看历史记录
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.DMM_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消操作，收回弹窗
                dialog.dismiss();
            }
        });

    }


}

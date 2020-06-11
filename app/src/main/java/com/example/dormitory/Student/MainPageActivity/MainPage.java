package com.example.dormitory.Student.MainPageActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.dormitory.R;

public class MainPage extends Fragment {
    private CardView toChange;//申请换宿舍
    private  CardView toRepair;//宿舍报修
    private CheckBox CheBox_state;//左上角状态
    private TextView tv_detail;//左上角“查看详情”
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.mainpage_layout, container, false);
        //绑定4个控件
        toChange=view.findViewById(R.id.change);
        toRepair=view.findViewById(R.id.repair);
        CheBox_state=view.findViewById(R.id.main_cb_state);
        tv_detail=view.findViewById(R.id.main_tv_detail);
        //给“查看详情”设置下划线
        tv_detail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        //声明点击事件
        toChange.setOnClickListener(new ButtonListener());
        toRepair.setOnClickListener(new ButtonListener());
        CheBox_state.setOnClickListener(new ButtonListener());
        tv_detail.setOnClickListener(new ButtonListener());
        return view;
    }
    //查看详情弹出的内容
    private void popDetail(){
        //设置弹窗
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        //弹窗图标
        builder.setIcon(R.drawable.select_item_info);
        //弹窗标题
        builder.setTitle("详情");
        //弹窗内容
        builder.setMessage("开启“换宿意向”后，您有可能收到来自他人的换宿舍申请；\n关闭“换宿意向”后，您将不会收到来自他人的换宿舍申请；\n若您提交了换宿舍申请，则在申请处理完成之前将自动开启“换宿意向”；\n为避免频繁更改状态，每人每天只能更改一次。");
        //弹窗关闭按钮
        builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
        //弹窗显示与关闭
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    private class ButtonListener implements View.OnClickListener{
        public void onClick(View v){
            switch (v.getId()){
                case R.id.change:{
                    toChange.setClickable(false);//跳转到申请换宿舍页面
                    Intent intent = new Intent(getActivity(), changeDormitory.class);
                    startActivity(intent);
                    toChange.setClickable(true);
                    break;
                }
                case R.id.repair:{
                    toRepair.setClickable(false);//跳转到宿舍报修页面
                    Intent intent = new Intent(getActivity(), repairDormitory.class);
                    startActivity(intent);
                    toRepair.setClickable(true);
                    break;
                }
                case R.id.main_cb_state:{
                    if(CheBox_state.isChecked())
                    Toast.makeText(getActivity(),"已开启",Toast.LENGTH_SHORT).show();
                    else
                    Toast.makeText(getActivity(),"已关闭",Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.main_tv_detail:{
                    //弹出详情框
                    popDetail();
                    break;
                }
                default:break;
            }
        }
    }

}

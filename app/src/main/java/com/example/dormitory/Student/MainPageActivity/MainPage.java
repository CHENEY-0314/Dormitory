package com.example.dormitory.Student.MainPageActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

//import com.example.dormitory.MainActivity;
import com.example.dormitory.R;

public class MainPage extends Fragment {
    private CardView toChange;//申请换宿舍
    private  CardView toRepair;//宿舍报修
    private Switch switch_state;//左上角状态
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.mainpage_layout, container, false);
        //绑定控件
        toChange=view.findViewById(R.id.change);
        toRepair=view.findViewById(R.id.repair);
        switch_state=view.findViewById(R.id.switch_state);

        //声明点击事件
        toChange.setOnClickListener(new ButtonListener());
        toRepair.setOnClickListener(new ButtonListener());
        switch_state.setOnClickListener(new ButtonListener());
        return view;
    }
    //查看详情弹出的内容
    private void popDetail(){
        //设置弹窗
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        //弹窗图标
        builder.setIcon(R.drawable.select_item_info);
        //弹窗标题
        builder.setTitle("是否切换状态");
        //弹窗内容
        builder.setMessage("开启“换宿意向”后，您有可能收到来自他人的换宿舍申请；\n关闭“换宿意向”后，您将不会收到来自他人的换宿舍申请；\n若您提交了换宿舍申请，则在申请处理完成之前将自动开启“换宿意向”；\n为避免频繁更改状态，每人每天只能更改一次。");
        //弹窗点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击确定，则提示当前状态
                if(switch_state.isChecked()){
                    Toast.makeText(getActivity(),"已开启",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(),"已关闭",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击取消，则回退状态
                if(switch_state.isChecked()){
                    switch_state.setChecked(false);
                    Toast.makeText(getActivity(),"已取消",Toast.LENGTH_SHORT).show();
                }
                else{
                    switch_state.setChecked(true);
                    Toast.makeText(getActivity(),"已取消",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //设置点击弹窗外不可关闭弹窗
        builder.setCancelable(false);
        //显示弹窗
        builder.show();
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
                case R.id.switch_state:{
                    //切换状态，出现再次确认弹窗
                    popDetail();
                    break;
                }
                default:break;
            }
        }
    }

}

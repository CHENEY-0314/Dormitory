package com.example.dormitory.Student.MainPageActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.dormitory.R;
import com.example.dormitory.Student.NotePageActivity.TimeDifCalculater;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class MainPage extends Fragment {
    private CardView toChange;//申请换宿舍
    private  CardView toRepair;//宿舍报修
    private Switch switch_state;//左上角状态
    String firstopentime="2020:06:18:10:00";
    TimeDifCalculater timeDifCalculater;
    private SharedPreferences mUser;
    private SharedPreferences.Editor mUserEditor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.mainpage_layout, container, false);
        //绑定控件
        toChange=view.findViewById(R.id.change);
        toRepair=view.findViewById(R.id.repair);
        switch_state=view.findViewById(R.id.switch_state);

        mUser=getActivity().getSharedPreferences("userdata",MODE_PRIVATE);
        mUserEditor=mUser.edit();

        //声明点击事件
        toChange.setOnClickListener(new ButtonListener());
        toRepair.setOnClickListener(new ButtonListener());
        switch_state.setOnClickListener(new ButtonListener());
        switch_state.setChecked(mUser.getBoolean("switch_state",false));
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
                timeDifCalculater=new TimeDifCalculater(mUser.getString("firstopentime",null));
                String dw=timeDifCalculater.getTimeDif().substring(timeDifCalculater.getTimeDif().length()-1);
                if(dw.equals("年")||dw.equals("月")||dw.equals("天"))
                {setFirstopentimechangable(true);
                setSwitch_state_enable(true);}
                if(mUser.getBoolean("switch_state_enable",false)){
                //点击确定，则提示当前状态
                if(switch_state.isChecked()){
                    Toast.makeText(getActivity(),"已开启",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(),"已关闭",Toast.LENGTH_SHORT).show();
                }
                setSwitch_state_enable(false);
                }else{
                    if(switch_state.isChecked()){
                        Toast.makeText(getActivity(),"暂无开启次数，请稍后重试",Toast.LENGTH_SHORT).show();
                        switch_state.setChecked(false);
                    }
                    else{
                        Toast.makeText(getActivity(),"暂无关闭次数，请稍后重试",Toast.LENGTH_SHORT).show();
                        switch_state.setChecked(true);
                    }
                }
                mUserEditor.putBoolean("switch_state",switch_state.isChecked());
                mUserEditor.apply();
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
                    getFirstOpenTime();
                    //切换状态，出现再次确认弹窗
                    popDetail();
                    test();
                    break;
                }
                default:break;
            }
        }
    }
    public void getFirstOpenTime(){
        if(mUser.getString("firstopentime",null)==null)
          {mUserEditor.putString("firstopentime",firstopentime);
          setFirstopentimechangable(true);
          setSwitch_state_enable(true);
          mUserEditor.putBoolean("switch_state",false);
          mUserEditor.apply();}
        if(mUser.getBoolean("firstopentimechangable",false))
        {SimpleDateFormat dff = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        firstopentime = dff.format(new Date());
        mUserEditor.putString("firstopentime",firstopentime);
        setFirstopentimechangable(false);
        mUserEditor.apply();
        }
    }

    public void setFirstopentimechangable(Boolean state) {
        mUserEditor.putBoolean("firstopentimechangable",state);
        mUserEditor.apply();
    }
    public void setSwitch_state_enable(Boolean state) {
        mUserEditor.putBoolean("switch_state_enable",state);
        mUserEditor.apply();
    }
    void test(){
        System.out.println(mUser.getString("firstopentime",null));
        System.out.println(mUser.getBoolean("switch_state_enable",false));
        System.out.println(mUser.getBoolean("firstopentimechangable",false));
    }
}

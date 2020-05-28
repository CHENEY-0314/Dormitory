package com.example.dormitory;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dormitory.MyPagesActivity.AdviceActivity;
import com.example.dormitory.MyPagesActivity.ChangeUserImgActivity;
import com.example.dormitory.MyPagesActivity.SetupActivity;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPage extends Fragment {

    private CircleImageView Userimage;
    private TextView musername,mdormitory;
    private LinearLayout mChangeDormitory,mFixDormitory,mSetUp,mAdvice;

    private View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.mypage_layout, container, false);
        init(); //声明控件，初始化控件
        return mView;
    }

    //声明控件，初始化控件
    private void init(){
        //声明控件
        Userimage=mView.findViewById(R.id.MyPage_userimg);
        musername= mView.findViewById(R.id.MyPage_txt_username);
        mdormitory= mView.findViewById(R.id.MyPage_txt_home);
        mChangeDormitory= mView.findViewById(R.id.MyPage_btn_ChangeD);
        mFixDormitory= mView.findViewById(R.id.MyPage_btn_FixD);
        mSetUp= mView.findViewById(R.id.MyPage_btn_setup);
        mAdvice= mView.findViewById(R.id.MyPage_btn_advice);

        //声明点击事件
        Userimage.setOnClickListener(new ButtonListener());
        mChangeDormitory.setOnClickListener(new ButtonListener());
        mFixDormitory.setOnClickListener(new ButtonListener());
        mSetUp.setOnClickListener(new ButtonListener());
        mAdvice.setOnClickListener(new ButtonListener());
    }

    //处理点击事件
    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.MyPage_userimg:  //跳转到更换头像页面
                    Userimage.setClickable(false);
                    Intent intent = new Intent(getActivity(), ChangeUserImgActivity.class);
                    startActivity(intent);
                    Userimage.setClickable(true);
                    break;
                case R.id.MyPage_btn_ChangeD: //跳转到我的更换宿舍申请页面
                    mChangeDormitory.setClickable(false);
                    //Intent intent2 = new Intent(getActivity(), ChangeDormitoryActivity.class);
                    //startActivity(intent2);
                    mChangeDormitory.setClickable(true);
                    break;
                case R.id.MyPage_btn_FixD:  //跳转到我的维修申请页面
                    mFixDormitory.setClickable(false);
                    //Intent intent3 = new Intent(getActivity(), FixDormitoryActivity.class);
                    //startActivity(intent3);
                    mFixDormitory.setClickable(true);
                    break;
                case R.id.MyPage_btn_setup:  //跳转到设置页面
                    mSetUp.setClickable(false);
                    Intent intent4 = new Intent(getActivity(), SetupActivity.class);
                    startActivity(intent4);
                    mSetUp.setClickable(true);
                    break;
                case R.id.MyPage_btn_advice:
                    mAdvice.setClickable(false);
                    Intent intent5 = new Intent(getActivity(), AdviceActivity.class);
                    startActivity(intent5);
                    mAdvice.setClickable(true);
                    break;
                default:break;
            }
        }
    }

    }

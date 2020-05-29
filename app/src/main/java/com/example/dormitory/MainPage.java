package com.example.dormitory;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class MainPage extends Fragment {
    private CardView toChange;
    private  CardView toRepair;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.mainpage_layout, container, false);
        //声明两个控件
        toChange=view.findViewById(R.id.change);
        toRepair=view.findViewById(R.id.repair);

        //声明点击事件
        toChange.setOnClickListener(new ButtonListener());
        toRepair.setOnClickListener(new ButtonListener());
        return view;
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
                default:break;
            }
        }
    }

}

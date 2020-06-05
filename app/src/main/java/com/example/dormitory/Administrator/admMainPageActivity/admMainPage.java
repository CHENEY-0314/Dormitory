package com.example.dormitory.Administrator.admMainPageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.dormitory.R;


public class admMainPage extends Fragment {
    private CardView toQuery;
    private  CardView toApply;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.adm_mainpage_layout, container, false);
        //绑定两个控件
        toQuery=view.findViewById(R.id.query);
        toApply=view.findViewById(R.id.apply);

        //声明点击事件
        toQuery.setOnClickListener(new admMainPage.ButtonListener());
        toApply.setOnClickListener(new admMainPage.ButtonListener());
        return view;
    }

    private class ButtonListener implements View.OnClickListener{
        public void onClick(View v){
            switch (v.getId()){
                case R.id.change:{

                }
                case R.id.repair:{

                }
                default:break;
            }
        }
    }
}

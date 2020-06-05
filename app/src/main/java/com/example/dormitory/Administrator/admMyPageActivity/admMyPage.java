package com.example.dormitory.Administrator.admMyPageActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dormitory.R;

public class admMyPage extends Fragment {
    private TextView admName,admNum;
    private LinearLayout mLogout;

    private View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.adm_mypage_layout, container, false);

        return mView;
    }
}

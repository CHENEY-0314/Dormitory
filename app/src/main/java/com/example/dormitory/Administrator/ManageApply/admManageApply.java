package com.example.dormitory.Administrator.ManageApply;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dormitory.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class admManageApply extends AppCompatActivity {
    static final int Num_Items=2;
    private List<Fragment> fragmentList=new ArrayList<Fragment>();
    private String[] strings=new String[]{"宿舍报修","换宿申请"};
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_manage_apply);

        fragmentList.add(new ManageRDFragment());
        fragmentList.add(new ManageCDFragment());

        //初始化控件
        init();

        //点击返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admManageApply.this.finish();
            }
        });
    }

    private void init(){
        back=findViewById(R.id.back);
        TabLayout tabLayout=findViewById(R.id.manageApply_tab_layout);
        ViewPager viewPager=findViewById(R.id.manageApply_viewPager);
        MyAdapter fragmentAdapter=new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public class MyAdapter extends FragmentPagerAdapter{
        public MyAdapter(FragmentManager fm){
            super(fm);
        }

        @NonNull
        @Override
        public int getCount() {
            return Num_Items;
        }
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
        @Override
        public CharSequence getPageTitle(int position){
            return strings[position];
        }
    }
}


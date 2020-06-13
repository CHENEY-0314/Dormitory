package com.example.dormitory.Student.MyPagesActivity.MyChangeDormitory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dormitory.R;
import com.example.dormitory.Student.MyPagesActivity.MyChangeDormitory.MyCDFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MyChangeDormitoryActivity extends AppCompatActivity {

    static final int NUM_ITEMS = 2;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private String[] strings = new String[]{" 我的申请 "," 接受的申请 "};

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_change_dormitory);
        fragmentList.add(new MyCDFragment());
        fragmentList.add(new RecelveCDFragment());
        initView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyChangeDormitoryActivity.this.finish();
            }
        });
    }

    private void initView(){
        back=findViewById(R.id.MCD_back);
        TabLayout tab_layout = findViewById(R.id.MCD_tab_layout);
        ViewPager viewPager = findViewById(R.id.MCD_viewPager);
        MyAdapter fragmentAdater = new  MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdater);
        tab_layout.setupWithViewPager(viewPager);
    }

    public class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return strings[position];
        }
    }
}

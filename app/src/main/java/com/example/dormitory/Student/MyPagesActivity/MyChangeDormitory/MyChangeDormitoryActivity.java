package com.example.dormitory.Student.MyPagesActivity.MyChangeDormitory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

    private ImageView back,more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_change_dormitory);
        fragmentList.add(new MyCDFragment());
        fragmentList.add(new RecelveCDFragment());

        initView();  //初始化空间函数

        //点击返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyChangeDormitoryActivity.this.finish();
            }
        });

        //点击右上角弹窗
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });

    }

    private void initView(){
        back=findViewById(R.id.MCD_back);
        more=findViewById(R.id.MCD_more);
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

    //展开弹窗函数
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
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialog.findViewById(R.id.DMM_history_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查看历史更换宿舍记录
                dialog.dismiss();
            }
        });

        //取消操作，收回弹窗
        dialog.findViewById(R.id.DMM_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

}

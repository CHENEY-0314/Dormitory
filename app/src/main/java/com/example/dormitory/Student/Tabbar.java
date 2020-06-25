package com.example.dormitory.Student;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;


import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.dormitory.R;
import com.example.dormitory.Student.Adapters.SectionsPagerAdapter;
import com.example.dormitory.Student.MainPageActivity.MainPage;
import com.example.dormitory.Student.MyPagesActivity.MyPage;
import com.example.dormitory.Student.NotePageActivity.NotePage;

import java.util.ArrayList;
import java.util.List;

public class Tabbar extends AppCompatActivity{
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private BottomNavigationBar bottomNavigationBar;

    private long exitTime=0;  //用于判断两次点击退出主页的事件间隔

    private final String ONE = "one";
    private final String TWO = "two";
    private final String THREE = "three";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottomtabbar_layout);

        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);

        viewPager = findViewById(R.id.viewPager);
        //将所有的fragment放入一个ArrayList中
        fragmentList = new ArrayList<Fragment>();
        Fragment f1 = new MainPage();
        Fragment f2 = new NotePage();
        Fragment f3 = new MyPage();
        fragmentList.add(f1);
        fragmentList.add(f2);
        fragmentList.add(f3);
        FragmentManager fragmentManager = getSupportFragmentManager();
        SectionsPagerAdapter myPagerAdapter = new SectionsPagerAdapter(fragmentManager, fragmentList);

        //将viewPager设置Adapter
        viewPager.setAdapter(myPagerAdapter);
        //设置初始化的位置（即第0个位置，开始的位置）
        viewPager.setCurrentItem(0);
        //当做出动作时，通过此监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //滑动页面之后做的事，这里与BottomNavigationBar连接，设置BottomNavigationBar。这是我们可以通过滑动改变BottomNavigationBar
                bottomNavigationBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationBar
                .setBarBackgroundColor(R.color.loginback)//底部导航栏背景颜色
                .setInActiveColor(R.color.white) //未选中状态颜色
                .setActiveColor(R.color.colorAccent)  //选中状态颜色
                .setMode(BottomNavigationBar.MODE_FIXED_NO_TITLE )
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC) //导航栏背景模式（图标的动画样式和这个有关系 变大了）
                .addItem(new BottomNavigationItem(R.drawable.mainpage_active_icon, "主页").setActiveColorResource(R.color.green)
                        .setInactiveIconResource(R.drawable.mainpage_nactive_icon).setInActiveColorResource(R.color.white))
                .addItem(new BottomNavigationItem(R.drawable.homepage_active_icon, "通知").setActiveColorResource(R.color.green)
                        .setInactiveIconResource(R.drawable.homepage_nactive_icon).setInActiveColorResource(R.color.white))
                .addItem(new BottomNavigationItem(R.drawable.mypage_active_icon, "我的").setActiveColorResource(R.color.green)
                        .setInactiveIconResource(R.drawable.mypage_nactive_icon).setInActiveColorResource(R.color.white))
                .setFirstSelectedPosition(0)//初始化之后第一个选中的位置
                .initialise();

        //当对BottomNavigationBar进行操作时，通过下列函数监听。
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                //被选中时，改变viewPager中的位置进而得到fragment的数据，即改变fragment。
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {   //重写返回函数以实现点击两次退出APP
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast toast=Toast.makeText(Tabbar.this,null,Toast.LENGTH_SHORT);
            toast.setText("再按一次退出程序");
            toast.show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);  //更改跳转动画
        }
    }

}


package com.example.dormitory;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.hjm.bottomtabbar.BottomTabBar;

public class Tabbar extends AppCompatActivity {


    int overPosition;
    private BottomTabBar bottom_tab_bar;
    private float mPosX, mPosY, mCurPosX, mCurPosY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottomtabbar_layout);
        initView();

        initData();
    }

    private void initData() {

        setGestureListener();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事

        setContentView(R.layout.bottomtabbar_layout);
        bottom_tab_bar = findViewById(R.id.bottom_tab_bar);
        bottom_tab_bar.init(getSupportFragmentManager())
                .setImgSize(25, 25)
                .setFontSize(10)
                .setTabPadding(5, 5, 5)
                .setChangeColor(getResources().getColor(R.color.green), getResources().getColor(R.color.normal))
                .addTabItem("主页", R.drawable.icon_tab_home_normal, MainPage.class)
                .addTabItem("首页", R.drawable.icon_tab_home_normal, HomePage.class)
                .addTabItem("我的", R.drawable.icon_tab_home_normal, MyPage.class)
                .setTabBarBackgroundColor(getResources().getColor(R.color.tabbar))
                .isShowDivider(true)
                .setOnTabChangeListener(new BottomTabBar.OnTabChangeListener() {
                    @Override
                    public void onTabChange(int position, String name, View view) {
                        Log.i("TGA", "位置：" + position + "      选项卡的文字内容：" + name);
//                        mViewPager.setCurrentItem(position);
                        overPosition = position;
                    }//添加选项卡切换监听
                })
                .setCurrentTab(0);//设置当前选中的tab,从0开始


    }

    private void setGestureListener() {
        bottom_tab_bar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        mPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurPosX = event.getX();
                        mCurPosY = event.getY();

                        break;
                    case MotionEvent.ACTION_UP:
                        if (mCurPosX - mPosX > 0 && (Math.abs(mCurPosX - mPosX) > 25)) {
                            //向左滑動
                            bottom_tab_bar.setCurrentTab(overPosition - 1);

                        } else if (mCurPosX - mPosX < 0 && (Math.abs(mCurPosX - mPosX) > 25)) {
                            //向右滑动
                            bottom_tab_bar.setCurrentTab(overPosition + 1);
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void initView() {
        bottom_tab_bar = (BottomTabBar) findViewById(R.id.bottom_tab_bar);
    }
}


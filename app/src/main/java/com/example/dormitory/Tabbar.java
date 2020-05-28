package com.example.dormitory;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.hjm.bottomtabbar.BottomTabBar;

public class Tabbar extends AppCompatActivity {

    BottomTabBar bottomtabbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色，简单粗暴直接完事
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottomtabbar_layout);
        bottomtabbar = findViewById(R.id.bottom_tab_bar);
        bottomtabbar.init(getSupportFragmentManager())
                .setImgSize(30, 30)
                .setFontSize(11)
                .setTabPadding(5, 5, 5)
                .setChangeColor(getResources().getColor(R.color.green), getResources().getColor(R.color.normal))
                .addTabItem("主页", R.drawable.icon_tab_home_normal, MainPage.class)
                .addTabItem("首页", R.drawable.icon_tab_home_normal, HomePage.class)
                .addTabItem("我的", R.drawable.icon_tab_home_normal, MyPage.class)
                .setTabBarBackgroundColor(getResources().getColor(R.color.tabbar))
                .isShowDivider(false);
    }
}

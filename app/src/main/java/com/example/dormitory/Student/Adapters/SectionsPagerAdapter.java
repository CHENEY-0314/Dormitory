package com.example.dormitory.Student.Adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

public class SectionsPagerAdapter extends FragmentPagerAdapter {


    private FragmentManager fragmentManager;
    private List<Fragment> fragments;

    //这里传入的是一个FragmentManger以及一个Fragment的列表，也就是所有需要的Fragment都放入这个队列中。
    public SectionsPagerAdapter(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.fragments = mFragments;
        this.fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}

package com.ilmare.oschina.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：6/15/2016 11:33 PM
 * 版本号： 1.0
 * 版权所有(C) 6/15/2016
 * 描述：
 * ===============================
 */

public class BaseViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments;
    ArrayList<String> titles;

    public BaseViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments=new ArrayList<>();
        titles=new ArrayList<>();
    }

    public void setPagerData(Fragment fragment,String title){
        fragments.add(fragment);
        titles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}

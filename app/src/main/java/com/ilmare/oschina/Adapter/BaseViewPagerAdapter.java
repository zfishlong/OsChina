package com.ilmare.oschina.Adapter;

import android.content.Context;
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

   private ArrayList<FragmentInfo> fragmentInfos;

    private  ArrayList<String> titles;

    private Context mContext;
    public BaseViewPagerAdapter(Context mContext,FragmentManager fm) {
        super(fm);
        this.mContext=mContext;
        fragmentInfos=new ArrayList<>();
        titles=new ArrayList<>();

    }

    public void setPagerData(FragmentInfo fragmentInfo,String title){
        fragmentInfos.add(fragmentInfo);
        titles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        FragmentInfo info=fragmentInfos.get(position);
        return Fragment.instantiate(mContext,info.getClazz().getName(),info.getBundle()) ;
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

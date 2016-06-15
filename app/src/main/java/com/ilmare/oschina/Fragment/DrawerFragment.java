package com.ilmare.oschina.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ilmare.oschina.R;
import com.ilmare.oschina.Utils.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：6/15/2016 10:46 AM
 * 版本号： 1.0
 * 版权所有(C) 6/15/2016
 * 描述：
 * ===============================
 */
public class DrawerFragment extends Fragment {

    @InjectView(R.id.menu_item_quests)
    LinearLayout menuItemQuests;
    @InjectView(R.id.menu_item_opensoft)
    LinearLayout menuItemOpensoft;
    @InjectView(R.id.menu_item_blog)
    LinearLayout menuItemBlog;
    @InjectView(R.id.menu_item_gitapp)
    LinearLayout menuItemGitapp;
    @InjectView(R.id.menu_item_rss)
    LinearLayout menuItemRss;
    @InjectView(R.id.menu_item_setting)
    LinearLayout menuItemSetting;
    @InjectView(R.id.menu_item_exit)
    LinearLayout menuItemExit;


    //接口回调
    private OnDrawerItemSelectedListener drawerItemSelectedListener;

    public static interface OnDrawerItemSelectedListener {
        void onDrawerItemSelected(int position);
    }

    public void setOnDrawerItemSelectedListener(OnDrawerItemSelectedListener listener){
        drawerItemSelectedListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_navigation_drawer, null);
        ButterKnife.inject(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.menu_item_quests, R.id.menu_item_opensoft, R.id.menu_item_blog, R.id.menu_item_gitapp, R.id.menu_item_rss, R.id.menu_item_setting, R.id.menu_item_exit})
    public void onClick(View view) {
        if(drawerItemSelectedListener!=null){
            drawerItemSelectedListener.onDrawerItemSelected(view.getId());
        }
    }
}

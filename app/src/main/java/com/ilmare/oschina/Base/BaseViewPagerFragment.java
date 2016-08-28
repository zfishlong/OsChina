package com.ilmare.oschina.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilmare.oschina.Adapter.BaseViewPagerAdapter;
import com.ilmare.oschina.R;
import com.ilmare.oschina.Widget.PagerSlidingTab;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：6/15/2016 11:28 PM
 * 版本号： 1.0
 * 版权所有(C) 6/15/2016
 * 描述：综合和动弹的Fragment抽取
 * ===============================
 */
public abstract class BaseViewPagerFragment extends Fragment {

    @InjectView(R.id.pager_tabstrip)
    PagerSlidingTab pagerTabstrip;

    @InjectView(R.id.pager)
    ViewPager pager;

    private BaseViewPagerAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.base_viewpage_fragment, null);
        ButterKnife.inject(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //创建数据
        adapter = new BaseViewPagerAdapter(getActivity(),getChildFragmentManager());
        //子类填充数据
        setPagerData(adapter);
        //设置适配器
        pager.setAdapter(adapter);
        //绑定ViewPager
        pagerTabstrip.setViewPager(pager);
        //全部缓存下来
        setOffscreenPageLimit();

    }

    //设置缓存数量
    private void setOffscreenPageLimit() {
        pager.setOffscreenPageLimit(adapter.getCount()-1);
    }

    public abstract void setPagerData(BaseViewPagerAdapter adapter);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}

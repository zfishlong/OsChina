package com.ilmare.oschina.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ilmare.oschina.Base.BaseFragment;
import com.ilmare.oschina.R;
import com.ilmare.oschina.Utils.UIHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：7/28/2016 10:33 AM
 * 版本号： 1.0
 * 版权所有(C) 7/28/2016
 * 描述：
 * ===============================
 */

public class ExploreFragment extends BaseFragment {
    @InjectView(R.id.rl_active)
    LinearLayout rlActive;
    @InjectView(R.id.rl_find_osc)
    LinearLayout rlFindOsc;
    @InjectView(R.id.rl_city)
    LinearLayout rlCity;
    @InjectView(R.id.rl_activities)
    LinearLayout rlActivities;
    @InjectView(R.id.rl_scan)
    LinearLayout rlScan;
    @InjectView(R.id.rl_shake)
    LinearLayout rlShake;

    @Override
    protected void init() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_explore;
    }

    @Override
    protected void onLoadSuccess(String content) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.rl_active, R.id.rl_find_osc, R.id.rl_city, R.id.rl_activities, R.id.rl_scan, R.id.rl_shake})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_active: //朋友圈
                UIHelper.showMyActive(getActivity());
                break;
            case R.id.rl_find_osc: //找人
                break;
            case R.id.rl_city:   // 同城 -->gone
                break;
            case R.id.rl_activities: //活动
                break;
            case R.id.rl_scan:  //扫一扫
                break;
            case R.id.rl_shake: //摇一摇
                break;
        }
    }
}

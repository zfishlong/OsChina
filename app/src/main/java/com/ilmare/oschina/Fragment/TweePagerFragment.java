package com.ilmare.oschina.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ilmare.oschina.Adapter.BaseViewPagerAdapter;
import com.ilmare.oschina.Adapter.FragmentInfo;
import com.ilmare.oschina.Base.BaseViewPagerFragment;
import com.ilmare.oschina.R;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：6/16/2016 12:16 PM
 * 版本号： 1.0
 * 版权所有(C) 6/16/2016
 * 描述：
 * ===============================
 */

public class TweePagerFragment extends BaseViewPagerFragment {


    @Override
    public void setPagerData(BaseViewPagerAdapter adapter) {
        String[] stringArray = getActivity().getResources().getStringArray(R.array.tweets_viewpage_arrays);
        adapter.setPagerData(new FragmentInfo(DefaultFragment.class,getBundle(stringArray[0])),stringArray[0]);
        adapter.setPagerData(new FragmentInfo(DefaultFragment.class,getBundle(stringArray[1])),stringArray[1]);
        adapter.setPagerData(new FragmentInfo(DefaultFragment.class,getBundle(stringArray[2])),stringArray[2]);

    }


    private Bundle getBundle(String catalog) {
        Bundle bundle = new Bundle();
        bundle.putString("key", "我是动弹里的: " + catalog);
        return bundle;
    }



}

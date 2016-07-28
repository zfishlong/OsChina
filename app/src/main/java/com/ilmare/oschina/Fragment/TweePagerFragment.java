package com.ilmare.oschina.Fragment;

import android.os.Bundle;

import com.ilmare.oschina.Adapter.BaseViewPagerAdapter;
import com.ilmare.oschina.Adapter.FragmentInfo;
import com.ilmare.oschina.Base.BaseListViewFragment;
import com.ilmare.oschina.Base.BaseViewPagerFragment;
import com.ilmare.oschina.Beans.TweetsList;
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

        adapter.setPagerData(new FragmentInfo(TweetsFragment.class,getBundle(TweetsList.CATALOG_LATEST)),stringArray[0]);
        adapter.setPagerData(new FragmentInfo(TweetsFragment.class,getBundle(TweetsList.CATALOG_HOT)),stringArray[1]);
        adapter.setPagerData(new FragmentInfo(TweetsFragment.class,getBundle(TweetsList.CATALOG_ME)),stringArray[2]);

    }


    private Bundle getBundle(int catalog) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListViewFragment.BUNDLE_KEY_CATALOG, catalog);
        return bundle;
    }



}

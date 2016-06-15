package com.ilmare.oschina.Fragment;

import com.ilmare.oschina.Adapter.BaseViewPagerAdapter;
import com.ilmare.oschina.Base.BaseViewPagerFragment;
import com.ilmare.oschina.R;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：6/15/2016 11:52 PM
 * 版本号： 1.0
 * 版权所有(C) 6/15/2016
 * 描述：综合
 * ===============================
 */

public class ZongHeFragment extends BaseViewPagerFragment {


    private String[] stringArray;

    @Override
    public void setPagerData(BaseViewPagerAdapter fragmentAdapter) {

        stringArray = getActivity().getResources().getStringArray(R.array.news_viewpage_arrays);
        fragmentAdapter.setPagerData(new DefaultFragment(), stringArray[0]);
        fragmentAdapter.setPagerData(new DefaultFragment(), stringArray[1]);
        fragmentAdapter.setPagerData(new DefaultFragment(), stringArray[2]);
        fragmentAdapter.setPagerData(new DefaultFragment(), stringArray[3]);

    }
}

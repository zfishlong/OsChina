package com.ilmare.oschina.Fragment;

import android.os.Bundle;

import com.ilmare.oschina.Adapter.BaseViewPagerAdapter;
import com.ilmare.oschina.Adapter.FragmentInfo;
import com.ilmare.oschina.Base.BaseViewPagerFragment;
import com.ilmare.oschina.Beans.BlogList;
import com.ilmare.oschina.R;
/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：6/15/2016 11:52 PM
 * 版本号： 1.0
 * 版权所有(C) 6/15/2016
 * 描述：综合界面-->
 * ===============================
 */
public class ZongHePagerFragment extends BaseViewPagerFragment {

    private String[] stringArray;

    @Override
    public void setPagerData(BaseViewPagerAdapter fragmentAdapter) {
        stringArray = getActivity().getResources().getStringArray(R.array.news_viewpage_arrays);
        fragmentAdapter.setPagerData(new FragmentInfo(AllNewsFragment.class,getBundle(stringArray[0])), stringArray[0]);
        fragmentAdapter.setPagerData(new FragmentInfo(HotNewsFragment.class,getBundle(stringArray[1])), stringArray[1]);
        fragmentAdapter.setPagerData(new FragmentInfo(BlogNewsFragment.class,getBundle(stringArray[2])), stringArray[2]);
        fragmentAdapter.setPagerData(new FragmentInfo(BlogNewsFragment.class,getBundle(stringArray[3])), stringArray[3]);
    }


    private Bundle getBundle(String catalog) {
        Bundle bundle = new Bundle();
        if(catalog.equals(stringArray[2])){
            bundle.putString(BlogNewsFragment.BUNDLE_BLOG_TYPE, BlogList.CATALOG_LATEST);
        }else if(catalog.equals(stringArray[3])){
            bundle.putString(BlogNewsFragment.BUNDLE_BLOG_TYPE, BlogList.CATALOG_RECOMMEND);
        }
        return bundle;
    }

}

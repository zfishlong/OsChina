package com.ilmare.oschina.Fragment;

import com.ilmare.oschina.Adapter.ActiveAdapter;
import com.ilmare.oschina.Base.BaseListViewFragment;
import com.ilmare.oschina.Net.OSChinaApi;
import com.ilmare.oschina.UI.AppContext;
import com.ilmare.oschina.Utils.XmlUtils;
import com.ilmare.oschina.Beans.ActiveList;


import java.io.Serializable;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：8/4/2016 12:47 PM
 * 版本号： 1.0
 * 版权所有(C) 8/4/2016
 * 描述：
 * ===============================
 */

public class ActiveFragment extends BaseListViewFragment {

    protected static final String TAG = ActiveFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "active_list";
    private boolean mIsWatingLogin=false; // 还没登陆

    @Override
    protected void loadMoreFromServer() {

    }

    @Override
    protected String getCacheKeyPrefix() {
        return new StringBuffer(CACHE_KEY_PREFIX + mCatalog).append(
                AppContext.getInstance().getLoginUid()).toString();
    }

    @Override
    protected void executeOnReadCacheSuccess(Serializable seri) {

    }

    @Override
    protected void loadFromServer() {
        OSChinaApi.getActiveList(AppContext.getInstance().getLoginUid(),
                mCatalog, mCurrentPage, mHandler);
    }

    @Override
    protected void onLoadSuccess(String content) {
        ActiveList list = XmlUtils.toBean(ActiveList.class, content.getBytes());
        listview.setAdapter(new ActiveAdapter(getActivity(),list));
    }
}

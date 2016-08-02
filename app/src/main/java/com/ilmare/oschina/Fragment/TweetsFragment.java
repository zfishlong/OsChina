package com.ilmare.oschina.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.ilmare.oschina.Adapter.TweetAdapter;
import com.ilmare.oschina.Base.BaseListViewFragment;
import com.ilmare.oschina.Beans.Tweet;
import com.ilmare.oschina.Beans.TweetsList;
import com.ilmare.oschina.Net.OSChinaApi;
import com.ilmare.oschina.Utils.UIHelper;
import com.ilmare.oschina.Utils.XmlUtils;

import java.io.Serializable;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：7/5/2016 1:44 PM
 * 版本号： 1.0
 * 版权所有(C) 7/5/2016
 * 描述：动弹 fragment
 * ===============================
 */

public class TweetsFragment extends BaseListViewFragment implements AdapterView.OnItemClickListener {

    private int mCurrentPage=0;
    private TweetAdapter adapter;

    private static final String CACHE_KEY_PREFIX = "tweetslist_";
    private TweetsList tweetsList;

    @Override
    protected String getCacheKeyPrefix() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String str = bundle.getString("topic");
            if (str != null) {
                return str;
            }
        }
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected void executeOnReadCacheSuccess(Serializable seri) {
        System.out.println("我加载缓存" + getCacheKeyPrefix());
        tweetsList= (TweetsList) seri;
        adapter = new TweetAdapter(getActivity(), tweetsList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }

    @Override
    protected void loadFromServer() {
        Bundle bundle = getArguments();
        int uid = bundle.getInt(BaseListViewFragment.BUNDLE_KEY_CATALOG, 0);
//      TODO 登录判断
//      TODO 加载更多
        OSChinaApi.getTweetList(uid, mCurrentPage, mHandler);
    }

    @Override
    protected void onLoadSuccess(String content) {
        tweetsList = XmlUtils.toBean(TweetsList.class, content.getBytes());
        adapter = new TweetAdapter(getActivity(), tweetsList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
        saveLocal(tweetsList);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Tweet tweet = adapter.getItem(position);
        if (tweet != null) {
            UIHelper.showTweetDetail(view.getContext(), tweet, tweet.getId());
        }
    }



}

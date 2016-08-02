package com.ilmare.oschina.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

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
    private int uid;
    private boolean isLoadMore=false;
    @Override
    protected void loadMoreFromServer() {
        if(!isLoadMore){
            isLoadMore=true;
            mCurrentPage++;
            loadFromServer();
        }
    }



    @Override
    protected String getCacheKeyPrefix() {
        Bundle bundle = getArguments();
        uid = bundle.getInt(BaseListViewFragment.BUNDLE_KEY_CATALOG, 0);
        return CACHE_KEY_PREFIX + mCatalog;
    }


    @Override
    protected void executeOnReadCacheSuccess(Serializable seri) {
        tweetsList= (TweetsList) seri;
        if(isLoadMore){
            if(tweetsList.getList().size()==0){
                Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
                mCurrentPage--;
            }else{
                adapter.addDatas(tweetsList);
                adapter.notifyDataSetChanged();
            }

            isLoadMore=false;
        } else {
            adapter = new TweetAdapter(getActivity(), tweetsList);
            listview.setAdapter(adapter);
        }
        listview.setOnItemClickListener(this);
    }

    @Override
    protected void loadFromServer() {
//      TODO 登录判断
//      TODO 加载更多
        OSChinaApi.getTweetList(uid, mCurrentPage, mHandler);
    }

    @Override
    protected void onLoadSuccess(String content) {
        tweetsList = XmlUtils.toBean(TweetsList.class, content.getBytes());
        if(isLoadMore){
            if(tweetsList.getList().size()==0){
                Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
                mCurrentPage--;
            }else{
                adapter.addDatas(tweetsList);
                adapter.notifyDataSetChanged();
            }

            isLoadMore=false;
        } else {
            adapter = new TweetAdapter(getActivity(), tweetsList);
            listview.setAdapter(adapter);
        }

        listview.setOnItemClickListener(this);
        saveLocal(tweetsList);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //
        if(position==adapter.getCount()) return ;

        Tweet tweet = adapter.getItem(position);
        if (tweet != null) {
            UIHelper.showTweetDetail(view.getContext(), tweet, tweet.getId());
        }
    }



}

package com.ilmare.oschina.Fragment;

import android.view.View;
import android.widget.AdapterView;

import com.ilmare.oschina.Adapter.NewsListViewAdapter;
import com.ilmare.oschina.Base.BaseListViewFragment;
import com.ilmare.oschina.Beans.News;
import com.ilmare.oschina.Beans.NewsList;
import com.ilmare.oschina.Net.OSChinaApi;
import com.ilmare.oschina.Utils.UIHelper;
import com.ilmare.oschina.Utils.XmlUtils;

import java.io.Serializable;


/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：6/16/2016 12:38 PM
 * 版本号： 1.0
 * 版权所有(C) 6/16/2016
 * 描述：
 * ===============================
 */

public class HotNewsFragment extends BaseListViewFragment implements AdapterView.OnItemClickListener {

    private NewsList newsList;

//    private int mCurrentPage = 1;  //当前页
    private int mCatalog = 4;    //页分类
    private NewsListViewAdapter newsListViewAdapter;
    private static final String CACHE_KEY_PREFIX = "newslist_";

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX+mCatalog;
    }


    @Override
    protected void executeOnReadCacheSuccess(Serializable seri) {
        System.out.println("我加载缓存" + getCacheKeyPrefix());
        newsList= (NewsList) seri;
        newsListViewAdapter = new NewsListViewAdapter(newsList,getActivity());
        listview.setAdapter(newsListViewAdapter);
        listview.setOnItemClickListener(this);
    }



    @Override
    protected void loadFromServer() {
        OSChinaApi.getNewsList(mCatalog, mCurrentPage, mHandler);
    }



    @Override
    protected void onLoadSuccess(String content) {
        newsList = XmlUtils.toBean(NewsList.class, content.getBytes());
        newsListViewAdapter = new NewsListViewAdapter(newsList,getActivity());
        listview.setAdapter(newsListViewAdapter);
        listview.setOnItemClickListener(this);
        saveLocal(newsList);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO 加入已读列表
        News news= (News) newsListViewAdapter.getItem(position);
        UIHelper.showNewsRedirect(getActivity(),news);
    }
}

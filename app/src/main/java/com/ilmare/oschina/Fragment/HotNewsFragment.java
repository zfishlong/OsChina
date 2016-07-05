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
import com.loopj.android.http.RequestParams;


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

    private int mCurrentPage = 1;  //当前页
    private int mCatalog = 4;    //页分类
    private NewsListViewAdapter newsListViewAdapter;

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
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO 加入已读列表
        News news= (News) newsListViewAdapter.getItem(position);
        UIHelper.showNewsRedirect(getActivity(),news);
    }
}

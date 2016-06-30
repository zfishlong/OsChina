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
 * 描述：资讯界面
 * ===============================
 */

public class AllNewsFragment extends BaseListViewFragment implements AdapterView.OnItemClickListener {

    private NewsList newsList;
    private int mCurrentPage = 1;  //当前页
    private int mCatalog = 1;    //页分类
    private NewsListViewAdapter newsListViewAdapter;

    @Override
    protected void loadFromServer() {
//      方式一：直接创建使用 简单粗暴
//      new AsyncHttpClient().post(getUrl(),getParams(),mHandler);

//      方式二：获取单列的直接使用
//      ApiHttpClient.getHttpClient().post(getUrl(),getParams(),mHandler);

//      方式三: 直接用封装了AsyncHttpClient的ApiHttpClient静态方法, 封装路径和拼接参数, 麻烦
//      ApiHttpClient.post("action/api/news_list", getParams(), mHandler);

//      方式四: (推荐)主线程 mCatalog = 1 资讯, mCatalog = 4 热点
        OSChinaApi.getNewsList(mCatalog, mCurrentPage, mHandler);
    }

    @Override
    protected void onLoadSuccess(String content) {
        newsList = XmlUtils.toBean(NewsList.class, content.getBytes());
        newsListViewAdapter = new NewsListViewAdapter(newsList, getActivity());
        listview.setAdapter(newsListViewAdapter);
        listview.setOnItemClickListener(this);
    }

    protected String getUrl() {
        return "http://www.oschina.net/action/api/news_list";
    }

    public RequestParams getParams() {
        RequestParams params = new RequestParams();
        params.put("pageIndex", "0");
        params.put("catalog", "1");
        params.put("pageSize", "20");
        return params;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News news= (News) newsListViewAdapter.getItem(position);
        //Todo 跳转页面 加入已读列表
        if (news != null) {
            UIHelper.showNewsRedirect(view.getContext(), news);
            // 放入已读列表
//            saveToReadedList(view, NewsList.PREF_READED_NEWS_LIST, news.getId()
//                    + "");
        }
    }
}

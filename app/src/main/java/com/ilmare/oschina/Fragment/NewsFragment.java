package com.ilmare.oschina.Fragment;

import android.widget.ListView;

import com.ilmare.oschina.Adapter.NewsListViewAdapter;
import com.ilmare.oschina.Base.BaseListViewFragment;
import com.ilmare.oschina.Beans.NewsList;
import com.ilmare.oschina.Utils.XmlUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
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

public class NewsFragment<News> extends BaseListViewFragment {

    private NewsList newsList;

    @Override
    protected void setDataToListView(ListView listview) {

    }

    @Override
    protected void loadFromServer() {
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("pageIndex","1");
        params.put("catalog","1");
        params.put("pageSize","20");
        client.post("http://www.oschina.net/action/api/news_list", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                newsList = XmlUtils.toBean(NewsList.class, content.getBytes());
                NewsListViewAdapter adapter=new NewsListViewAdapter(newsList,getActivity());
                listview.setAdapter(adapter);
            }
        });
    }



}

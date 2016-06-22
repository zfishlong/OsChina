package com.ilmare.oschina.Fragment;

import com.ilmare.oschina.Adapter.NewsListViewAdapter;
import com.ilmare.oschina.Base.BaseListViewFragment;
import com.ilmare.oschina.Beans.NewsList;
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

public class BlogNewsFragment extends BaseListViewFragment {

    private NewsList newsList;

    @Override
    protected void loadFromServer() {

    }

    @Override
    protected void onLoadSuccess(String content) {
//        newsList = XmlUtils.toBean(NewsList.class, content.getBytes());
//        NewsListViewAdapter adapter=new NewsListViewAdapter(newsList,getActivity());
//        listview.setAdapter(adapter);
    }

    protected String getUrl() {
        return "http://www.oschina.net/action/api/blog_list";
    }

    public RequestParams getParams() {
        RequestParams params=new RequestParams();
        params.put("pageIndex","0");
        params.put("type","latest");
        params.put("pageSize","20");
        return params;
    }


}

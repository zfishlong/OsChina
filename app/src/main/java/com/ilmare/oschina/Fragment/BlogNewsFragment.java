package com.ilmare.oschina.Fragment;

import android.os.Bundle;

import com.ilmare.oschina.Adapter.BlogListAdapter;
import com.ilmare.oschina.Adapter.NewsListViewAdapter;
import com.ilmare.oschina.Base.BaseListViewFragment;
import com.ilmare.oschina.Beans.BlogList;
import com.ilmare.oschina.Beans.NewsList;
import com.ilmare.oschina.Net.OSChinaApi;
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


    public static final String BUNDLE_BLOG_TYPE = "BUNDLE_BLOG_TYPE";
    private String blogType;
    private int mCurrentPage=0;
    private BlogList blogList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            blogType = args.getString(BUNDLE_BLOG_TYPE);  //获取
        }
    }

    @Override
    protected void loadFromServer() {
       OSChinaApi.getBlogList(blogType, mCurrentPage, mHandler);
    }

    @Override
    protected void onLoadSuccess(String content) {
        blogList = XmlUtils.toBean(BlogList.class, content.getBytes());
        BlogListAdapter adapter=new BlogListAdapter(getActivity(),blogList);
        listview.setAdapter(adapter);
    }


}

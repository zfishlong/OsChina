package com.ilmare.oschina.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.ilmare.oschina.Adapter.BlogListAdapter;
import com.ilmare.oschina.Base.BaseListViewFragment;
import com.ilmare.oschina.Beans.Blog;
import com.ilmare.oschina.Beans.BlogList;
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
public class BlogNewsFragment extends BaseListViewFragment implements AdapterView.OnItemClickListener {

    private String blogType;
    private BlogList blogList;
    private BlogListAdapter blogListAdapter;
    public static final String BUNDLE_BLOG_TYPE = "BUNDLE_BLOG_TYPE";
    private static final String CACHE_KEY_PREFIX = "bloglist_";
    private boolean isLoadMore=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            blogType = args.getString(BUNDLE_BLOG_TYPE);  //获取
        }
    }


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
        return CACHE_KEY_PREFIX+blogType;
    }


    @Override
    protected void executeOnReadCacheSuccess(Serializable seri) {
        blogList= (BlogList) seri;

        if(isLoadMore){
            if(blogList.getList().size()==0){
                Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                mCurrentPage--;
            }else{
                blogListAdapter.addDatas(blogList);
                blogListAdapter.notifyDataSetChanged();
            }
            isLoadMore=false;
        }else{
            blogListAdapter = new BlogListAdapter(getActivity(),blogList);
            listview.setAdapter(blogListAdapter);
        }
        listview.setOnItemClickListener(this);
    }


    @Override
    protected void loadFromServer() {
       OSChinaApi.getBlogList(blogType, mCurrentPage, mHandler);
    }


    @Override
    protected void onLoadSuccess(String content) {
        blogList = XmlUtils.toBean(BlogList.class, content.getBytes());

        if(isLoadMore){
            if(blogList.getList().size()==0){
                Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                mCurrentPage--;
            }else{
                blogListAdapter.addDatas(blogList);
                blogListAdapter.notifyDataSetChanged();
            }
            isLoadMore=false;
        }else{
            blogListAdapter = new BlogListAdapter(getActivity(),blogList);
            listview.setAdapter(blogListAdapter);
        }
        listview.setOnItemClickListener(this);
        saveLocal(blogList);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Blog blog = (Blog) blogListAdapter.getItem(position);

        if (blog != null) {
            UIHelper.showBlogDetail(getActivity(), blog.getId(),
                    blog.getCommentCount());

            //TODO 保存到已读列表
//            saveToReadedList(view, BlogList.PREF_READED_BLOG_LIST, blog.getId()
//                    + "");
        }
    }
}

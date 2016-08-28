package com.ilmare.oschina.Fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

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
 * 描述：资讯界面 ---
 * ===============================
 */
public class AllNewsFragment extends BaseListViewFragment
        implements AdapterView.OnItemClickListener {

    private NewsList newsList = new NewsList();         //消息列表
    private int mCatalog = 1;                           //类别-->资讯
    private NewsListViewAdapter newsListViewAdapter;    //适配器

    // 1.对应的缓存文件命名：缓存前缀+mCatalog+页数
    private static final String CACHE_KEY_PREFIX = "newslist_"; //缓存前缀

    //是否正在加载更多
    private boolean isLoadMore=false;

    //返回前缀
    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    //读取缓存的事件
    @Override
    protected void executeOnReadCacheSuccess(Serializable seri) {
        newsList = (NewsList) seri;
        if(isLoadMore){
            if(newsList.getList().size()==0){
                Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                mCurrentPage--;
            }else{
                newsListViewAdapter.addDatas(newsList);
                newsListViewAdapter.notifyDataSetChanged();
            }
            isLoadMore=false;
        }else{
            newsListViewAdapter = new NewsListViewAdapter(newsList, getActivity());
            listview.setAdapter(newsListViewAdapter);

        }
        listview.setOnItemClickListener(this);
    }

    //从服务器加载
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
        if(isLoadMore){  //如果是加载更多
            if(newsList.getList().size()==0){  //没有更多数据
                Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
                mCurrentPage--;
            }else{  //加载到更多数据
                newsListViewAdapter.addDatas(newsList);
                newsListViewAdapter.notifyDataSetChanged();
            }
            isLoadMore=false;
        }else{
            newsListViewAdapter = new NewsListViewAdapter(newsList, getActivity());
            listview.setAdapter(newsListViewAdapter);
        }

        //设置点击事件
        listview.setOnItemClickListener(this);

        //保存到本地
        saveLocal(newsList);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News news = (News) newsListViewAdapter.getItem(position);
        if (news != null) {
            UIHelper.showNewsRedirect(view.getContext(), news);

            //TODO 放入已读列表
            //saveToReadedList(view, NewsList.PREF_READED_NEWS_LIST, news.getId() + "");
        }
    }
}

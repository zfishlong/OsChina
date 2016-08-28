package com.ilmare.oschina.Base;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.ilmare.oschina.Cache.CacheManager;
import com.ilmare.oschina.R;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：6/16/2016 12:33 PM
 * 版本号： 1.0
 * 版权所有(C) 6/16/2016
 * 描述：综合和动弹中的7个界面 --->只用adapter不同
 * ===============================
 */
public abstract class BaseListViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    //ListView 和 SwipeRefreshLayout
    @InjectView(R.id.listview)
    protected ListView listview;
    @InjectView(R.id.swiperefreshlayout)
    protected SwipeRefreshLayout swiperefreshlayout;

    //当前页
    public int mCurrentPage=0;

    //当前分类
    public int mCatalog=1;

    public static final String BUNDLE_KEY_CATALOG ="bundle_key_catalog" ;

    //缓存任务
    private AsyncTask<String, Void, Serializable> mCacheTask;


    //创建一个View
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_pull_refresh_listview, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //请求数据 参数isNeedCache-->是否需要缓存
        requestData(true);

        //设置刷新监听
        swiperefreshlayout.setOnRefreshListener(this);

        //ListView设置 滚动监听
        listview.setOnScrollListener(this);
    }



    private String getCacheKey() {
        return new StringBuilder(getCacheKeyPrefix()).append("_")
                .append(mCurrentPage).toString();
    }


    //是否需要缓存
    private void requestData(boolean isNeedCache) {
        if(isNeedCache){
            String key = getCacheKey();
            readCacheData(key);
        }
        loadFromServer();
    }




    //读取缓存数据
    private void readCacheData(String cacheKey) {
        cancelReadCacheTask();
        mCacheTask = new CacheTask(getActivity()).execute(cacheKey);
    }


    //取消读取缓存数据
    private void cancelReadCacheTask() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE &&
                listview.getAdapter().getCount() == listview.getLastVisiblePosition() + 1) {
            //滑到最后了  加载更多
            loadMoreFromServer();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    //读取缓存的异步任务
    private class CacheTask extends AsyncTask<String, Void, Serializable> {
        private final WeakReference<Context> mContext;

        private CacheTask(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @Override
        protected Serializable doInBackground(String... params) {
            Serializable seri = CacheManager.readObject(mContext.get(),
                    params[0]);
            if (seri == null) {
                return null;
            } else {
                return seri;
            }
        }

        @Override
        protected void onPostExecute(Serializable seri) {
            super.onPostExecute(seri);

            if (seri != null) {
                executeOnReadCacheSuccess(seri);
            }
//            else {
//                executeOnLoadDataError(null);
//            }
//            executeOnLoadFinish();
        }
    }



    //异步缓存任务缓存任务
    private class SaveCacheTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<Context> mContext;
        private final Serializable seri;
        private final String key;

        private SaveCacheTask(Context context, Serializable seri, String key) {
            mContext = new WeakReference<Context>(context);
            this.seri = seri;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            CacheManager.saveObject(mContext.get(), seri, key);
            return null;
        }
    }

    //处理的事件
    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(String content) {
            onLoadSuccess(content);
            swiperefreshlayout.setRefreshing(false);
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);
            String key = getCacheKey();
            readCacheData(key);
            swiperefreshlayout.setRefreshing(false);
        }
    };

    //缓存到本地
    public void saveLocal(Serializable data){
        new SaveCacheTask(getActivity(), data, getCacheKey()).execute();
    }

    //下拉刷新时的事件
    @Override
    public void onRefresh() {
        mCurrentPage=0;
        loadFromServer();
    }


    //从服务器加载数据 -->有子类实现
    protected abstract void loadFromServer();

    //加载成功时-->由子类实现
    protected abstract void onLoadSuccess(String content);

    //读取缓存成功时-->由子类实现
    protected abstract void executeOnReadCacheSuccess(Serializable seri);

    //获取缓存的Key -->由子类实现
    protected abstract String getCacheKeyPrefix();

    //加载更多-->由子类实现
    protected abstract void loadMoreFromServer();


}

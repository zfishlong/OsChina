package com.ilmare.oschina.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ilmare.oschina.Adapter.NewsListViewAdapter;
import com.ilmare.oschina.AppContext;
import com.ilmare.oschina.Beans.NewsList;
import com.ilmare.oschina.R;
import com.ilmare.oschina.Utils.StringUtils;
import com.ilmare.oschina.Utils.XmlUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：6/16/2016 12:33 PM
 * 版本号： 1.0
 * 版权所有(C) 6/16/2016
 * 描述：
 * ===============================
 */
public abstract class BaseListViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.listview)
    protected ListView listview;
    @InjectView(R.id.swiperefreshlayout)
    protected SwipeRefreshLayout swiperefreshlayout;


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

        loadFromServer();
        swiperefreshlayout.setOnRefreshListener(this);
    }

    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(String content) {
            onLoadSuccess(content);
            swiperefreshlayout.setRefreshing(false);
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);
            Toast.makeText(getActivity(), content, 0).show();
            swiperefreshlayout.setRefreshing(false);
        }
    };

    protected abstract void loadFromServer();

    protected abstract void onLoadSuccess(String content);



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onRefresh() {
        loadFromServer();
    }


}

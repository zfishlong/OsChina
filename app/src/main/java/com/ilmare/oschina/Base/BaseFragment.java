package com.ilmare.oschina.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.ButterKnife;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：6/16/2016 12:32 PM
 * 版本号： 1.0
 * 版权所有(C) 6/16/2016
 * 描述：基类碎片的整理
 * ===============================
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=View.inflate(getActivity(),getLayoutId(),null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initData();
    }

    protected abstract void init();

    protected abstract void initData();

    protected abstract int getLayoutId();

    //点击事件
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(String content) {
            onLoadSuccess(content);
//            saveCache(entity);
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);
//            Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
//            readCacheData(getCacheKey());
        }
    };

    protected abstract void onLoadSuccess(String content);


}

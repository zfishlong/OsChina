package com.ilmare.oschina.DetailFragment;

import android.os.Bundle;

import com.ilmare.oschina.Base.BaseFragment;
import com.ilmare.oschina.R;
import com.ilmare.oschina.UI.DetailActivity;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：7/27/2016 10:24 PM
 * 版本号： 1.0
 * 版权所有(C) 7/27/2016
 * 描述：
 * ===============================
 */

public class TweetDetailFragment extends BaseFragment {

    private DetailActivity outAty;

    @Override
    protected void init() {
        outAty = (DetailActivity) getActivity();
    }

    @Override
    protected void initData() {
        Bundle bundle = outAty.getIntent().getExtras();

//        bundle.putInt("tweet_id", tweetid);
//        bundle.putInt(DetailActivity.BUNDLE_KEY_DISPLAY_TYPE,
//                DetailActivity.DISPLAY_TWEET);
//        if (tweet != null) {
//            bundle.putParcelable("tweet", tweet);
//        }


//        OSChinaApi.getCommentList(mTweetId, CommentList.CATALOG_TWEET,
//                mCurrentPage, mHandler);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pull_refresh_listview;
    }

    @Override
    protected void onLoadSuccess(String content) {

    }
}

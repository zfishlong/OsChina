package com.ilmare.oschina.Fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ilmare.oschina.Base.BaseFragment;
import com.ilmare.oschina.Beans.MyInformation;
import com.ilmare.oschina.Beans.User;
import com.ilmare.oschina.Cache.CacheManager;
import com.ilmare.oschina.Net.OSChinaApi;
import com.ilmare.oschina.R;
import com.ilmare.oschina.UI.AppContext;
import com.ilmare.oschina.Utils.StringUtils;
import com.ilmare.oschina.Utils.TDevice;
import com.ilmare.oschina.Utils.UIHelper;
import com.ilmare.oschina.Utils.XmlUtils;
import com.ilmare.oschina.Widget.AvatarView;
import com.ilmare.oschina.Widget.CircleImageView;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import butterknife.InjectView;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：7/27/2016 10:53 PM
 * 版本号： 1.0
 * 版权所有(C) 7/27/2016
 * 描述：
 * ===============================
 */

public class MyInformationFragment extends BaseFragment {


    @InjectView(R.id.iv_avatar)
    AvatarView ivAvatar;
    @InjectView(R.id.iv_gender)
    ImageView ivGender;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.iv_qr_code)
    ImageView ivQrCode;
    @InjectView(R.id.rl_user_center)
    RelativeLayout rlUserCenter;
    @InjectView(R.id.tv_score)
    TextView tvScore;
    @InjectView(R.id.ly_score)
    LinearLayout lyScore;
    @InjectView(R.id.tv_favorite)
    TextView tvFavorite;
    @InjectView(R.id.ly_favorite)
    LinearLayout lyFavorite;
    @InjectView(R.id.tv_following)
    TextView tvFollowing;
    @InjectView(R.id.ly_following)
    LinearLayout lyFollowing;
    @InjectView(R.id.tv_follower)
    TextView tvFollower;
    @InjectView(R.id.ly_follower)
    LinearLayout lyFollower;
    @InjectView(R.id.ll_user_container)
    LinearLayout llUserContainer;
    @InjectView(R.id.iv_avatar1)
    CircleImageView ivAvatar1;
    @InjectView(R.id.rl_user_unlogin)
    RelativeLayout rlUserUnlogin;
    @InjectView(R.id.tv_mes)
    TextView tvMes;
    @InjectView(R.id.rl_message)
    LinearLayout rlMessage;
    @InjectView(R.id.rl_blog)
    LinearLayout rlBlog;
    @InjectView(R.id.rl_note)
    LinearLayout rlNote;
    @InjectView(R.id.rl_team)
    LinearLayout rlTeam;
    @InjectView(R.id.rootview)
    LinearLayout rootview;
    private User mInfo;
    private AsyncTask<String, Void, User> mCacheTask;
    private boolean mIsWatingLogin=true;

    @Override
    protected void init() {

        lyFavorite.setOnClickListener(this);
        lyFollowing.setOnClickListener(this);
        lyFollower.setOnClickListener(this);
        lyScore.setOnClickListener(this);
        rlTeam.setOnClickListener(this);
        rlBlog.setOnClickListener(this);
        rlNote.setOnClickListener(this);
        rlUserUnlogin.setOnClickListener(this);
        ivQrCode.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        if(AppContext.getInstance().isLogin()) {
            mIsWatingLogin = false;
            String key = getCacheKey();
            if (TDevice.hasInternet() && (!CacheManager.isExistDataCache(getActivity(), key))) {
                int uid = AppContext.getInstance().getLoginUid();
                OSChinaApi.getMyInformation(uid, mHandler);
            } else {
                readCacheData(key);
            }
        }else{
            mIsWatingLogin = true;
        }


    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_information;
    }


    @Override
    protected void onLoadSuccess(String content) {
        mInfo = XmlUtils.toBean(MyInformation.class, content.getBytes()).getUser();
        if (mInfo != null) {
            fillUI();
            AppContext.getInstance().updateUserInfo(mInfo);
            //保存到本地
            new SaveCacheTask(getActivity(), mInfo, getCacheKey()).execute();
        }
    }

    private String getCacheKey() {
        return "my_information" + AppContext.getInstance().getLoginUid();
    }

    private void fillUI() {
        if (mInfo == null)
            return;
        ivAvatar.setAvatarUrl(mInfo.getPortrait());
        tvName.setText(mInfo.getName());
        ivGender.setImageResource(StringUtils.toInt(mInfo.getGender()) != 2 ? R.drawable.userinfo_icon_male
                : R.drawable.userinfo_icon_female);
        tvScore.setText(String.valueOf(mInfo.getScore()));
        tvFavorite.setText(String.valueOf(mInfo.getFavoritecount()));
        tvFollowing.setText(String.valueOf(mInfo.getFollowers()));
        tvFollower.setText(String.valueOf(mInfo.getFans()));
    }


    @Override
    public void onClick(View v) {

        if (mIsWatingLogin) {
            AppContext.showToast(R.string.unlogin);
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        switch (v.getId()) {
            case R.id.rl_note:  //
                AppContext.showToast("便签");
//                UIHelper.showSimpleBack(getActivity(),SimpleBackPage.NOTE);
                break;
            case R.id.rl_user_unlogin:
                AppContext.showToast("便签");
//                 UIHelper.showLoginActivity(getActivity());
                break;

        }
    }

    private void readCacheData(String key) {
        cancelReadCacheTask();
        mCacheTask = new CacheTask(getActivity()).execute(key);
    }

    private void cancelReadCacheTask() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }


    private class CacheTask extends AsyncTask<String, Void, User> {
        private final WeakReference<Context> mContext;

        private CacheTask(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @Override
        protected User doInBackground(String... params) {
            Serializable seri = CacheManager.readObject(mContext.get(),
                    params[0]);
            if (seri == null) {
                return null;
            } else {
                return (User) seri;
            }
        }

        @Override
        protected void onPostExecute(User info) {
            super.onPostExecute(info);
            if (info != null) {
                mInfo = info;
                fillUI();
            }
        }
    }

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
}

package com.ilmare.oschina.Fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ilmare.oschina.Base.BaseFragment;
import com.ilmare.oschina.Net.OSChinaApi;
import com.ilmare.oschina.Utils.XmlUtils;
import com.ilmare.oschina.Utils.StringUtils;
import com.ilmare.oschina.Beans.MyInformation;
import com.ilmare.oschina.Beans.User;
import com.ilmare.oschina.R;
import com.ilmare.oschina.UI.AppContext;
import com.ilmare.oschina.Utils.UIHelper;
import com.ilmare.oschina.Widget.AvatarView;
import com.ilmare.oschina.Widget.CircleImageView;

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
        int uid = AppContext.getInstance().getLoginUid();
        OSChinaApi.getMyInformation(uid, mHandler);
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
            //new SaveCacheTask(getActivity(), mInfo, getCacheKey()).execute();
        }
    }

    private void fillUI() {
        if (mInfo == null)
            return;
        ivAvatar.setAvatarUrl(mInfo.getPortrait());
        tvName.setText(mInfo.getName());
        ivGender
                .setImageResource(StringUtils.toInt(mInfo.getGender()) != 2 ? R.drawable.userinfo_icon_male
                        : R.drawable.userinfo_icon_female);
        tvScore.setText(String.valueOf(mInfo.getScore()));
        tvFavorite.setText(String.valueOf(mInfo.getFavoritecount()));
        tvFollowing.setText(String.valueOf(mInfo.getFollowers()));
        tvFollower.setText(String.valueOf(mInfo.getFans()));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_note:
//                UIHelper.showSimpleBack(getActivity(),SimpleBackPage.NOTE);
                break;
            case R.id.rl_user_unlogin:
                UIHelper.showLoginActivity(getActivity());
                break;
        }


    }
}

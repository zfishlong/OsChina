package com.ilmare.oschina.DetailFragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ilmare.oschina.Adapter.CommentAdapter;
import com.ilmare.oschina.Base.BaseFragment;
import com.ilmare.oschina.Beans.CommentList;
import com.ilmare.oschina.Beans.Tweet;
import com.ilmare.oschina.Net.OSChinaApi;
import com.ilmare.oschina.R;
import com.ilmare.oschina.UI.AppContext;
import com.ilmare.oschina.Utils.KJAnimations;
import com.ilmare.oschina.Utils.StringUtils;
import com.ilmare.oschina.Utils.UIHelper;
import com.ilmare.oschina.Utils.XmlUtils;
import com.ilmare.oschina.Widget.AvatarView;

import butterknife.InjectView;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：7/27/2016 10:24 PM
 * 版本号： 1.0
 * 版权所有(C) 7/27/2016
 * 描述：
 * ===============================
 */

public class TweetDetailFragment extends BaseFragment implements OnItemLongClickListener {

    @InjectView(R.id.listview)
    ListView listview;

    @InjectView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;


    AvatarView ivAvatar;
    TextView tvName;
    TextView tvTime;
    TextView tvFrom;
    WebView webview;
    ImageView tweetImgRecord;
    TextView tweetTvRecord;
    RelativeLayout tweetBgRecord;
    TextView tvCommentCount;
    LinearLayout llComment;
    ImageView ivLikeState;
    LinearLayout llLike;
    TextView tvLikeusers;


    private Tweet mTweet;
    private int mTweetId;
    private int mCurrentPage=0;

    //private final RecordButtonUtil util = new RecordButtonUtil();

    @Override
    protected void init() {
    }

    @Override
    protected void initData() {

        listview.addHeaderView(initHeaderView());

        fillUI();

        OSChinaApi.getCommentList(mTweetId, CommentList.CATALOG_TWEET,
                mCurrentPage, mHandler);


    }
    private void fillUI() {

        ivAvatar.setAvatarUrl(mTweet.getPortrait());
        ivAvatar.setUserInfo(mTweet.getAuthorid(), mTweet.getAuthor());
        tvName.setText(mTweet.getAuthor());
        tvTime.setText(StringUtils.friendly_time(mTweet.getPubDate()));

        switch (mTweet.getAppclient()) {
            default:
                tvFrom.setVisibility(View.GONE);
                break;
            case Tweet.CLIENT_MOBILE:
                tvFrom.setText(R.string.from_mobile);
                break;
            case Tweet.CLIENT_ANDROID:
                tvFrom.setText(R.string.from_android);
                break;
            case Tweet.CLIENT_IPHONE:
                tvFrom.setText(R.string.from_iphone);
                break;
            case Tweet.CLIENT_WINDOWS_PHONE:
                tvFrom.setText(R.string.from_windows_phone);
                break;
            case Tweet.CLIENT_WECHAT:
                tvFrom.setText(R.string.from_wechat);
                break;
        }

        tvCommentCount.setText(mTweet.getCommentCount() + "");

        if (StringUtils.isEmpty(mTweet.getAttach())) {
            tweetBgRecord.setVisibility(View.GONE);
        } else {
            tweetBgRecord.setVisibility(View.VISIBLE);
        }


        fillWebViewBody();
        setLikeUser();
        setLikeState();
    }



    private void setLikeUser() {
        if (mTweet == null || mTweet.getLikeUser() == null
                || mTweet.getLikeUser().isEmpty()) {
            tvLikeusers.setVisibility(View.GONE);
        } else {
            tvLikeusers.setVisibility(View.VISIBLE);
            mTweet.setLikeUsers(getActivity(), tvLikeusers, false);
        }
    }

    /**
     * 填充webview内容
     */
    private void fillWebViewBody() {
        StringBuffer body = new StringBuffer();
        body.append(UIHelper.WEB_STYLE + UIHelper.WEB_LOAD_IMAGES);

        StringBuilder tweetbody = new StringBuilder(mTweet.getBody());

        String tweetBody = TextUtils.isEmpty(mTweet.getImgSmall()) ? tweetbody
                .toString() : tweetbody.toString() + "<br/><img src=\""
                + mTweet.getImgSmall() + "\">";
        body.append(setHtmlCotentSupportImagePreview(tweetBody));

        UIHelper.addWebImageShow(getActivity(), webview);
        webview.loadDataWithBaseURL(null, body.toString(), "text/html",
                "utf-8", null);
    }

    /**
     * 添加图片放大支持
     *
     * @param body
     * @return
     */
    private String setHtmlCotentSupportImagePreview(String body) {
        // 过滤掉 img标签的width,height属性
        body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
        body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
        return body.replaceAll("(<img[^>]+src=\")(\\S+)\"",
                "$1$2\" onClick=\"javascript:mWebViewImageListener.showImagePreview('"
                        + mTweet.getImgBig() + "')\"");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pull_refresh_listview;
    }


    @Override
    protected void onLoadSuccess(String content) {
        CommentList list = XmlUtils.toBean(CommentList.class, content.getBytes());
        //创建adapter
        CommentAdapter adapter=new CommentAdapter(getActivity(),list);

        //设置给listView
        listview.setAdapter(adapter);
    }



    protected View initHeaderView() {

        Intent args = getActivity().getIntent();

        mTweetId = args.getIntExtra("tweet_id", 0);
        mTweet = (Tweet) args.getParcelableExtra("tweet");

        listview.setOnItemLongClickListener(this);
        View header = LayoutInflater.from(getActivity()).inflate(
                R.layout.list_header_tweet_detail, null);

        ivAvatar = (AvatarView) header.findViewById(R.id.iv_avatar);
        tvName = (TextView) header.findViewById(R.id.tv_name);
        tvFrom = (TextView) header.findViewById(R.id.tv_from);
        tvTime = (TextView) header.findViewById(R.id.tv_time);
        tvCommentCount = (TextView) header.findViewById(R.id.tv_comment_count);


        webview = (WebView) header.findViewById(R.id.webview);

        UIHelper.initWebView(webview);

//      initSoundView(header);
        llLike = (LinearLayout) header.findViewById(R.id.ll_like);

        llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeOption();
            }
        });

        tvLikeusers = (TextView) header.findViewById(R.id.tv_likeusers);
        ivLikeState = (ImageView) header.findViewById(R.id.iv_like_state);
        tweetBgRecord= (RelativeLayout) header.findViewById(R.id.tweet_bg_record);

        return header;
    }

    private void likeOption() {
        if (mTweet == null)
            return;
        if (mTweet.getAuthorid() == AppContext.getInstance().getLoginUid()) {
            AppContext.showToast("不能为自己点赞~");
            return;
        }

        if (AppContext.getInstance().isLogin()) {
            if (mTweet.getIsLike() == 1) {
                mTweet.setIsLike(0);
                mTweet.getLikeUser().remove(0);
                mTweet.setLikeCount(mTweet.getLikeCount() - 1);
                OSChinaApi.pubUnLikeTweet(mTweetId, mTweet.getAuthorid(),
                        null);
            } else {
                ivLikeState.setAnimation(KJAnimations.getScaleAnimation(1.5f,
                        300));
                mTweet.setIsLike(1);
                mTweet.getLikeUser().add(0,
                        AppContext.getInstance().getLoginUser());
                mTweet.setLikeCount(mTweet.getLikeCount() + 1);
                OSChinaApi
                        .pubLikeTweet(mTweetId, mTweet.getAuthorid(), null);
            }
            setLikeState();
            mTweet.setLikeUsers(getActivity(), tvLikeusers, false);
        } else {
            AppContext.showToast("先登陆再点赞~");
        }
    }


    private void setLikeState() {
        if (mTweet != null) {
            if (mTweet.getIsLike() == 1) {
                ivLikeState.setBackgroundResource(R.drawable.ic_likeed);
            } else {
                ivLikeState.setBackgroundResource(R.drawable.ic_unlike);
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }


}

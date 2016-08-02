package com.ilmare.oschina.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilmare.oschina.Beans.Tweet;
import com.ilmare.oschina.Beans.TweetsList;
import com.ilmare.oschina.Net.OSChinaApi;
import com.ilmare.oschina.R;
import com.ilmare.oschina.UI.AppContext;
import com.ilmare.oschina.UI.ImagePreviewActivity;
import com.ilmare.oschina.Utils.ImageUtils;
import com.ilmare.oschina.Utils.KJAnimations;
import com.ilmare.oschina.Utils.StringUtils;
import com.ilmare.oschina.Utils.UIHelper;
import com.ilmare.oschina.Widget.AvatarView;
import com.ilmare.oschina.Widget.MyLinkMovementMethod;
import com.ilmare.oschina.Widget.MyURLSpan;
import com.ilmare.oschina.Widget.TweetTextView;
import com.ilmare.oschina.emoji.InputHelper;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapCallBack;
import org.kymjs.kjframe.bitmap.BitmapHelper;
import org.kymjs.kjframe.utils.DensityUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：7/5/2016 2:02 PM
 * 版本号： 1.0
 * 版权所有(C) 7/5/2016
 * 描述：
 * ===============================
 */

public class TweetAdapter extends BaseAdapter {


    private  List<Tweet> list;
    private Context context;
    private Bitmap recordBitmap;
    private final KJBitmap kjb = new KJBitmap();

    public TweetAdapter(Context context,TweetsList tweetsList){
        this.context=context;
        this.list = tweetsList.getList();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Tweet getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        ViewHolder vh=null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = View.inflate(context, R.layout.list_cell_tweet, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final Tweet tweet = list.get(position);


        if (tweet.getAuthorid() ==  AppContext.getInstance().getLoginUid()) {
            vh.del.setVisibility(View.VISIBLE);
            vh.del.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
//                    optionDel(parent.getContext(), tweet, position);
               }
            });
        } else {
            vh.del.setVisibility(View.GONE);
        }


        vh.face.setUserInfo(tweet.getAuthorid(), tweet.getAuthor());
        vh.face.setAvatarUrl(tweet.getPortrait());
        vh.author.setText(tweet.getAuthor());
        vh.time.setText(StringUtils.friendly_time(tweet.getPubDate()));
        vh.content.setMovementMethod(MyLinkMovementMethod.a());
        vh.content.setFocusable(false);
        vh.content.setDispatchToParent(true);
        vh.content.setLongClickable(false);



        Spanned span = Html.fromHtml(tweet.getBody().trim());

        if (StringUtils.isEmpty(tweet.getAttach())) {
            span = InputHelper.displayEmoji(context.getResources(), span);
            vh.content.setText(span);
        } else {
            if (recordBitmap == null) {
                initRecordImg(parent.getContext());
            }
            ImageSpan recordImg = new ImageSpan(parent.getContext(),
                    recordBitmap);
            SpannableString str = new SpannableString("c");
            str.setSpan(recordImg, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            vh.content.setText(str);
            span = InputHelper.displayEmoji(context.getResources(), span);
            vh.content.append(span);
        }



        MyURLSpan.parseLinkText(vh.content, span);
        vh.commentcount.setText(tweet.getCommentCount() + "");

        showTweetImage(vh, tweet.getImgSmall(), tweet.getImgBig(),
                parent.getContext());
        tweet.setLikeUsers(context, vh.likeUsers, true);

        final ViewHolder vh1 = vh;

        if (tweet.getLikeUser() == null) {
            vh.likeOption.setVisibility(View.GONE);
        }

        vh.likeOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContext.getInstance().isLogin()) {
                    if (tweet.getAuthorid() == AppContext.getInstance()
                            .getLoginUid()) {
                        AppContext.showToast("不能给自己点赞~");
                    } else {
                        updateLikeState(vh1, tweet);
                    }
                } else {
                    AppContext.showToast("先登陆再赞~");
                    UIHelper.showLoginActivity(parent.getContext());
                }
            }
        });




        if (tweet.getIsLike() == 1) {
            vh.likeState.setBackgroundResource(R.drawable.ic_likeed);
        } else {
            vh.likeState.setBackgroundResource(R.drawable.ic_unlike);
        }
        vh.platform.setVisibility(View.VISIBLE);
        switch (tweet.getAppclient()) {
            case Tweet.CLIENT_MOBILE:
                vh.platform.setText(R.string.from_mobile);
                break;
            case Tweet.CLIENT_ANDROID:
                vh.platform.setText(R.string.from_android);
                break;
            case Tweet.CLIENT_IPHONE:
                vh.platform.setText(R.string.from_iphone);
                break;
            case Tweet.CLIENT_WINDOWS_PHONE:
                vh.platform.setText(R.string.from_windows_phone);
                break;
            case Tweet.CLIENT_WECHAT:
                vh.platform.setText(R.string.from_wechat);
                break;
            default:
                vh.platform.setText("");
                vh.platform.setVisibility(View.GONE);
                break;
        }


        return convertView;
    }



    private void updateLikeState(ViewHolder vh, Tweet tweet) {
        if (tweet.getIsLike() == 1) {
            tweet.setIsLike(0);
            tweet.setLikeCount(tweet.getLikeCount() - 1);
            if (!tweet.getLikeUser().isEmpty()) {
                tweet.getLikeUser().remove(0);
            }
            OSChinaApi.pubUnLikeTweet(tweet.getId(), tweet.getAuthorid(),
                    null);
            vh.likeState.setBackgroundResource(R.drawable.ic_unlike);
        } else {
            tweet.setIsLike(1);
            vh.likeState
                    .setAnimation(KJAnimations.getScaleAnimation(1.5f, 300));
            tweet.getLikeUser().add(0, AppContext.getInstance().getLoginUser());
            OSChinaApi
                    .pubLikeTweet(tweet.getId(), tweet.getAuthorid(), null);
            vh.likeState.setBackgroundResource(R.drawable.ic_likeed);
            tweet.setIsLike(1);
            tweet.setLikeCount(tweet.getLikeCount() + 1);
        }
        tweet.setLikeUsers(context, vh.likeUsers, true);
    }


    /**
     * 动态设置动弹列表图片显示规则
     *
     * @author kymjs
     */
    private void showTweetImage(final ViewHolder vh, String imgSmall,
                                final String imgBig, final Context context) {
        if (imgSmall != null && !TextUtils.isEmpty(imgSmall)) {
            kjb.display(vh.image, imgSmall, new BitmapCallBack() {
                @Override
                public void onPreLoad() {
                    super.onPreLoad();
                    vh.image.setImageResource(R.drawable.pic_bg);
                }

                @Override
                public void onSuccess(Bitmap bitmap) {
                    super.onSuccess(bitmap);
                    if (bitmap != null) {
                        bitmap = BitmapHelper.scaleWithXY(bitmap,
                                300 / bitmap.getHeight());
                        vh.image.setImageBitmap(bitmap);
                    }
                }
            });
            vh.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePreviewActivity.showImagePrivew(context, 0,
                            new String[]{imgBig});
                }
            });
            vh.image.setVisibility(AvatarView.VISIBLE);
        } else {
            vh.image.setVisibility(AvatarView.GONE);
        }
    }


    private void initRecordImg(Context cxt) {
        recordBitmap = BitmapFactory.decodeResource(cxt.getResources(),
                R.drawable.audio3);
        recordBitmap = ImageUtils.zoomBitmap(recordBitmap,
                DensityUtils.dip2px(cxt, 20f), DensityUtils.dip2px(cxt, 20f));
    }

    public void addDatas(TweetsList tweetsList) {
        list.addAll(tweetsList.getList());
    }

    static class ViewHolder {
        @InjectView(R.id.tv_tweet_name)
        TextView author;
        @InjectView(R.id.tv_tweet_time)
        TextView time;
        @InjectView(R.id.tweet_item)
        TweetTextView content;
        @InjectView(R.id.tv_tweet_comment_count)
        TextView commentcount;
        @InjectView(R.id.tv_tweet_platform)
        TextView platform;
        @InjectView(R.id.iv_tweet_face)
        AvatarView face;
        @InjectView(R.id.iv_tweet_image)
        ImageView image;
        @InjectView(R.id.iv_like_state)
        ImageView likeState;
        @InjectView(R.id.tv_del)
        TextView del;
        @InjectView(R.id.tv_likeusers)
        TextView likeUsers;
        @InjectView(R.id.ll_like)
        View likeOption;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}

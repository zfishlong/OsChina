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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilmare.oschina.Beans.Active;
import com.ilmare.oschina.Beans.Active.ObjectReply;
import com.ilmare.oschina.Beans.ActiveList;
import com.ilmare.oschina.Beans.Tweet;
import com.ilmare.oschina.R;
import com.ilmare.oschina.UI.ImagePreviewActivity;
import com.ilmare.oschina.Utils.ImageUtils;
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
 * 创建时间：8/4/2016 1:00 PM
 * 版本号： 1.0
 * 版权所有(C) 8/4/2016
 * 描述：好友圈
 * ===============================
 */
public class ActiveAdapter extends BaseAdapter {

    private List<Active> mList;
    private Context mcontext;
    private int rectSize;
    private final static String AT_HOST_PRE = "http://my.oschina.net";
    private final static String MAIN_HOST = "http://www.oschina.net";
    private Bitmap recordBitmap;
    private final KJBitmap kjb = new KJBitmap();
    public ActiveAdapter(Context context, ActiveList activeList) {
        this.mcontext = context;
        this.mList = activeList.getList();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        initImageSize(parent.getContext());
        if(convertView==null|| convertView.getTag() == null){
            convertView = View.inflate(mcontext, R.layout.list_cell_active, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        Active item = (Active) mList.get(position);

        vh.tvName.setText(item.getAuthor());
        vh.tvAction.setText(UIHelper.parseActiveAction(item.getObjectType(),
                item.getObjectCatalog(), item.getObjectTitle()));

        if (TextUtils.isEmpty(item.getMessage())) {
            vh.tvBody.setVisibility(View.GONE);
        } else {
            vh.tvBody.setMovementMethod(MyLinkMovementMethod.a());
            vh.tvBody.setFocusable(false);
            vh.tvBody.setDispatchToParent(true);
            vh.tvBody.setLongClickable(false);

            Spanned span = Html.fromHtml(modifyPath(item.getMessage()));

            if (!StringUtils.isEmpty(item.getTweetattach())) {
                if (recordBitmap == null) {
                    initRecordImg(parent.getContext());
                }
                ImageSpan recordImg = new ImageSpan(mcontext,recordBitmap);

                SpannableString str = new SpannableString("c");
                str.setSpan(recordImg, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                vh.tvBody.setText(str);
                span = InputHelper.displayEmoji(parent.getContext()
                        .getResources(), span);
                vh.tvBody.append(span);
            } else {
                span = InputHelper.displayEmoji(parent.getContext()
                        .getResources(), span);
                vh.tvBody.setText(span);
            }
            MyURLSpan.parseLinkText(vh.tvBody, span);
        }

        ObjectReply reply = item.getObjectReply();
        if (reply != null) {
            vh.tvReply.setMovementMethod(MyLinkMovementMethod.a());
            vh.tvReply.setFocusable(false);
            vh.tvReply.setDispatchToParent(true);
            vh.tvReply.setLongClickable(false);
            Spanned span = UIHelper.parseActiveReply(reply.objectName,
                    reply.objectBody);
            vh.tvReply.setText(span);//
            MyURLSpan.parseLinkText(vh.tvReply, span);
            vh.lyReply.setVisibility(TextView.VISIBLE);
        } else {
            vh.tvReply.setText("");
            vh.lyReply.setVisibility(TextView.GONE);
        }


        vh.tvTime.setText(StringUtils.friendly_time(item.getPubDate()));

        vh.tvTime.setVisibility(View.VISIBLE);
        switch (item.getAppClient()) {
            default:
                vh.tvFrom.setText(R.string.from_web); // 不显示
                vh.tvFrom.setVisibility(View.GONE);
                break;
            case Tweet.CLIENT_MOBILE:
                vh.tvFrom.setText(R.string.from_mobile);
                break;
            case Tweet.CLIENT_ANDROID:
                vh.tvFrom.setText(R.string.from_android);
                break;
            case Tweet.CLIENT_IPHONE:
                vh.tvFrom.setText(R.string.from_iphone);
                break;
            case Tweet.CLIENT_WINDOWS_PHONE:
                vh.tvFrom.setText(R.string.from_windows_phone);
                break;
            case Tweet.CLIENT_WECHAT:
                vh.tvFrom.setText(R.string.from_wechat);
                break;
        }

        if (item.getCommentCount() > 0) {
            vh.tvCommentCount.setText(String.valueOf(item.getCommentCount()));
            vh.tvCommentCount.setVisibility(View.VISIBLE);
        } else {
            vh.tvCommentCount.setVisibility(View.GONE);
        }

        vh.ivAvatar.setUserInfo(item.getAuthorId(), item.getAuthor());
        vh.ivAvatar.setAvatarUrl(item.getPortrait());

        if (!TextUtils.isEmpty(item.getTweetimage())) {
            setTweetImage(parent, vh, item);
        } else {
            vh.ivPic.setVisibility(View.GONE);
            vh.ivPic.setImageBitmap(null);
        }


        return convertView;
    }

    private void initImageSize(Context cxt) {
        if (cxt != null && rectSize == 0) {
            rectSize = (int) cxt.getResources().getDimension(R.dimen.space_100);
        } else {
            rectSize = 300;
        }
    }

    private void initRecordImg(Context cxt) {
        recordBitmap = BitmapFactory.decodeResource(cxt.getResources(),
                R.drawable.audio3);
        recordBitmap = ImageUtils.zoomBitmap(recordBitmap,
                DensityUtils.dip2px(cxt, 20f), DensityUtils.dip2px(cxt, 20f));
    }

    private String modifyPath(String message) {
        message = message.replaceAll("(<a[^>]+href=\")/([\\S]+)\"", "$1"
                + AT_HOST_PRE + "/$2\"");
        message = message.replaceAll(
                "(<a[^>]+href=\")http://m.oschina.net([\\S]+)\"", "$1"
                        + MAIN_HOST + "$2\"");
        return message;
    }


    /**
     * 动态设置图片显示样式
     *
     * @author kymjs
     */
    private void setTweetImage(final ViewGroup parent, final ViewHolder vh,
                               final Active item) {
        vh.ivPic.setVisibility(View.VISIBLE);

        kjb.display(vh.ivPic, item.getTweetimage(), R.drawable.pic_bg, rectSize,
                rectSize, new BitmapCallBack() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        super.onSuccess(bitmap);
                        if (bitmap != null) {
                            bitmap = BitmapHelper.scaleWithXY(bitmap, rectSize
                                    / bitmap.getHeight());
                            vh.ivPic.setImageBitmap(bitmap);
                        }
                    }
                });

        vh.ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePreviewActivity.showImagePrivew(parent.getContext(), 0,
                        new String[]{getOriginalUrl(item.getTweetimage())});
            }
        });
    }
    private String getOriginalUrl(String url) {
        return url.replaceAll("_thumb", "");
    }
    static class ViewHolder {
        @InjectView(R.id.iv_avatar)
        AvatarView ivAvatar;
        @InjectView(R.id.tv_name)
        TextView tvName;
        @InjectView(R.id.tv_time)
        TextView tvTime;
        @InjectView(R.id.tv_action)
        TextView tvAction;
        @InjectView(R.id.tv_action_name)
        TextView tvActionName;
        @InjectView(R.id.ly_action)
        LinearLayout lyAction;
        @InjectView(R.id.tv_body)
        TweetTextView tvBody;
        @InjectView(R.id.tv_reply)
        TweetTextView tvReply;
        @InjectView(R.id.ly_reply)
        LinearLayout lyReply;
        @InjectView(R.id.iv_pic)
        ImageView ivPic;
        @InjectView(R.id.tv_from)
        TextView tvFrom;
        @InjectView(R.id.tv_comment_count)
        TextView tvCommentCount;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}

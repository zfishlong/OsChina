package com.ilmare.oschina.Adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilmare.oschina.Beans.Comment;
import com.ilmare.oschina.Beans.Comment.Refer;
import com.ilmare.oschina.Beans.Comment.Reply;

import com.ilmare.oschina.Beans.CommentList;
import com.ilmare.oschina.Beans.Tweet;
import com.ilmare.oschina.R;
import com.ilmare.oschina.Utils.StringUtils;
import com.ilmare.oschina.Widget.AvatarView;
import com.ilmare.oschina.Widget.FloorView;
import com.ilmare.oschina.Widget.MyLinkMovementMethod;
import com.ilmare.oschina.Widget.MyURLSpan;
import com.ilmare.oschina.Widget.TweetTextView;
import com.ilmare.oschina.emoji.InputHelper;
import  java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：7/28/2016 12:56 PM
 * 版本号： 1.0
 * 版权所有(C) 7/28/2016
 * 描述：
 * ===============================
 */

public class CommentAdapter extends BaseAdapter {

    private Context mContext;
    private CommentList mList;

    public CommentAdapter(Context context, CommentList list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.getList().size();
    }

    @Override
    public Object getItem(int position) {
        return mList.getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_cell_comment, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder= (ViewHolder) convertView.getTag();
        }


        Comment item = mList.getList().get(position);
        // 若Authorid为0，则显示非会员
        holder.name.setText(item.getAuthor()
                + (item.getAuthorId() == 0 ? "(非会员)" : ""));

        holder.content.setMovementMethod(MyLinkMovementMethod.a());
        holder.content.setFocusable(false);
        holder.content.setDispatchToParent(true);
        holder.content.setLongClickable(false);

        Spanned span = Html.fromHtml(TweetTextView.modifyPath(item
                .getContent()));
        span = InputHelper.displayEmoji(parent.getContext().getResources(),
                span.toString());

        holder.content.setText(span);
        MyURLSpan.parseLinkText(holder.content, span);

        holder.time.setText(StringUtils.friendly_time(item.getPubDate()));

        holder.from.setVisibility(View.VISIBLE);
        switch (item.getAppClient()) {
            default:
                holder.from.setText("");
                holder.from.setVisibility(View.GONE);
                break;
            case Tweet.CLIENT_MOBILE:
                holder.from.setText(R.string.from_mobile);
                break;
            case Tweet.CLIENT_ANDROID:
                holder.from.setText(R.string.from_android);
                break;
            case Tweet.CLIENT_IPHONE:
                holder.from.setText(R.string.from_iphone);
                break;
            case Tweet.CLIENT_WINDOWS_PHONE:
                holder.from.setText(R.string.from_windows_phone);
                break;
            case Tweet.CLIENT_WECHAT:
                holder.from.setText(R.string.from_wechat);
                break;
        }

        // setup refers
        setupRefers(parent.getContext(), holder, item.getRefers());

        // setup replies
        setupReplies(parent.getContext(), holder, item.getReplies());

        holder.avatar.setAvatarUrl(item.getPortrait());
        holder.avatar.setUserInfo(item.getAuthorId(), item.getAuthor());
        return convertView;
    }

    private void setupRefers(Context context, ViewHolder vh, List<Refer> refers) {
        vh.refers.removeAllViews();
        if (refers == null || refers.size() <= 0) {
            vh.refers.setVisibility(View.GONE);
        } else {
            vh.refers.setVisibility(View.VISIBLE);

            vh.refers.setComments(refers);
        }
    }

    private void setupReplies(Context context, ViewHolder vh,
                              List<Reply> replies) {
        vh.relies.removeAllViews();
        if (replies == null || replies.size() <= 0) {
            vh.relies.setVisibility(View.GONE);
        } else {
            vh.relies.setVisibility(View.VISIBLE);

            // add count layout
            View countView = View.inflate(mContext,
                    R.layout.list_cell_reply_count, null);
            TextView count = (TextView) countView
                    .findViewById(R.id.tv_comment_reply_count);
            count.setText(context.getResources().getString(
                    R.string.comment_reply_count, replies.size()));
            vh.relies.addView(countView);

            // add reply item
            for (Reply reply : replies) {
                LinearLayout replyItemView = (LinearLayout) View.inflate(mContext,R.layout.list_cell_reply_name_content, null);

                replyItemView.setOrientation(LinearLayout.HORIZONTAL);

                replyItemView
                        .setBackgroundResource(R.drawable.comment_background);

                TextView name = (TextView) replyItemView
                        .findViewById(R.id.tv_reply_name);
                name.setText(reply.rauthor + ":");

                TweetTextView replyContent = (TweetTextView) replyItemView
                        .findViewById(R.id.tv_reply_content);
                replyContent.setMovementMethod(MyLinkMovementMethod.a());
                replyContent.setFocusable(false);
                replyContent.setDispatchToParent(true);
                replyContent.setLongClickable(false);
                Spanned rcontent = Html.fromHtml(reply.rcontent);
                replyContent.setText(rcontent);
                MyURLSpan.parseLinkText(replyContent, rcontent);

                vh.relies.addView(replyItemView);
            }
        }
    }


    static class ViewHolder {
        @InjectView(R.id.iv_avatar)
        AvatarView avatar;
        @InjectView(R.id.tv_name)
        TextView name;
        @InjectView(R.id.tv_time)
        TextView time;
        @InjectView(R.id.tv_from)
        TextView from;
        @InjectView(R.id.tv_content)
        TweetTextView content;
        @InjectView(R.id.ly_relies)
        LinearLayout relies;
        @InjectView(R.id.ly_refers)
        FloorView refers;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}

package com.ilmare.oschina.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilmare.oschina.UI.AppContext;
import com.ilmare.oschina.Beans.Blog;
import com.ilmare.oschina.Beans.BlogList;
import com.ilmare.oschina.R;
import com.ilmare.oschina.Utils.StringUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：6/30/2016 7:38 PM
 * 版本号： 1.0
 * 版权所有(C) 6/30/2016
 * 描述：
 * ===============================
 */
public class BlogListAdapter extends BaseAdapter {

    private Context context;
    private final List<Blog> list;

    public BlogListAdapter(Context context,BlogList blogList){
        this.context=context;
        this.list = blogList.getList();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = View.inflate(context,
                    R.layout.list_cell_news, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Blog blog = list.get(position);

        vh.tip.setVisibility(View.VISIBLE);
        if (blog.getDocumenttype() == Blog.DOC_TYPE_ORIGINAL) {
            vh.tip.setImageResource(R.mipmap.widget_original_icon);
        } else {
            vh.tip.setImageResource(R.mipmap.widget_repaste_icon);
        }

        vh.title.setText(blog.getTitle());

        if (AppContext.isOnReadedPostList(BlogList.PREF_READED_BLOG_LIST,
                blog.getId() + "")) {
            vh.title.setTextColor(parent.getContext().getResources()
                    .getColor(R.color.main_gray));
        } else {
            vh.title.setTextColor(parent.getContext().getResources()
                    .getColor(R.color.main_black));
        }

        vh.description.setVisibility(View.GONE);
        String description = blog.getBody();
        if (null != description && !StringUtils.isEmpty(description)) {
            vh.description.setVisibility(View.VISIBLE);
            vh.description.setText(description.trim());
        }

        vh.source.setText(blog.getAuthor());
        vh.time.setText(StringUtils.friendly_time(blog.getPubDate()));
        vh.comment_count.setText(blog.getCommentCount() + "");
        return convertView;
    }

    public void addDatas(BlogList blogList) {
        list.addAll(blogList.getList());
    }

    static class ViewHolder {

        @InjectView(R.id.tv_title)
        TextView title;
        @InjectView(R.id.tv_description)
        TextView description;
        @InjectView(R.id.tv_source)
        TextView source;
        @InjectView(R.id.tv_time)
        TextView time;
        @InjectView(R.id.tv_comment_count)
        TextView comment_count;
        @InjectView(R.id.iv_tip)
        ImageView tip;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }


}

package com.ilmare.oschina.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilmare.oschina.UI.AppContext;
import com.ilmare.oschina.Beans.News;
import com.ilmare.oschina.Beans.NewsList;
import com.ilmare.oschina.R;
import com.ilmare.oschina.Utils.StringUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：6/16/2016 11:17 PM
 * 版本号： 1.0
 * 版权所有(C) 6/16/2016
 * 描述：
 * ===============================
 */

public class NewsListViewAdapter extends BaseAdapter {

    private   List<News> list;
    private Context context;

    public NewsListViewAdapter(NewsList newsList,Context context){
        this.context=context;
       this.list= newsList.getList();
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

        ViewHolder viewHolder;
        if(convertView==null){
            convertView=View.inflate(context,R.layout.list_cell_news,null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        News news = list.get(position);

        viewHolder.title.setText(news.getTitle());

        if (AppContext.isOnReadedPostList(NewsList.PREF_READED_NEWS_LIST,
                news.getId() + "")) {
            viewHolder.title.setTextColor(parent.getContext().getResources()
                    .getColor(R.color.main_gray));
        } else {
            viewHolder.title.setTextColor(parent.getContext().getResources()
                    .getColor(R.color.main_black));
        }

        String description = news.getBody();
        viewHolder.description.setVisibility(View.GONE);
        if (description != null && !StringUtils.isEmpty(description)) {
            viewHolder.description.setVisibility(View.VISIBLE);
            viewHolder.description.setText(description.trim());
        }

        viewHolder.source.setText(news.getAuthor());
        viewHolder.time.setText(StringUtils.friendly_time(news.getPubDate()));
        if (StringUtils.isToday(news.getPubDate())) {
            viewHolder.tip.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tip.setVisibility(View.GONE);
        }

        viewHolder.comment_count.setText(news.getCommentCount() + "");

        return convertView;
    }

    public void addDatas(NewsList newsList) {
        if(list==null){
            list=newsList.getList();
        }else{
            list.addAll(newsList.getList());
        }

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
        @InjectView(R.id.iv_link)
        ImageView link;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}

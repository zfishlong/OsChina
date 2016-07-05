package com.ilmare.oschina.DetailFragment;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.ilmare.oschina.Base.BaseFragment;
import com.ilmare.oschina.Beans.Blog;
import com.ilmare.oschina.Beans.BlogDetail;
import com.ilmare.oschina.Beans.NewsDetail;
import com.ilmare.oschina.Net.OSChinaApi;
import com.ilmare.oschina.R;
import com.ilmare.oschina.Utils.StringUtils;
import com.ilmare.oschina.Utils.UIHelper;
import com.ilmare.oschina.Utils.XmlUtils;
import com.ilmare.oschina.Widget.CustomerScrollView;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：7/5/2016 11:00 AM
 * 版本号： 1.0
 * 版权所有(C) 7/5/2016
 * 描述：
 * ===============================
 */

public class BlogDetailFragment extends BaseFragment {

    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_time)
    TextView tvTime;
    @InjectView(R.id.tv_source)
    TextView tvSource;
    @InjectView(R.id.webview)
    WebView webview;
    @InjectView(R.id.sv_news_container)
    CustomerScrollView svNewsContainer;

    private int mCommentCount;
    private int mBlogId;
    private Blog mBlog;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_detail;
    }

    @Override
    protected void init() {
        mCommentCount = getActivity().getIntent().getIntExtra("comment_count", 0);
        mBlogId = getActivity().getIntent().getIntExtra("blog_id", 0);
        UIHelper.initWebView(webview);
    }

    @Override
    protected void initData() {  //请求网络
        OSChinaApi.getBlogDetail(mBlogId, mHandler);
    }


    @Override
    protected void onLoadSuccess(String content) {
        mBlog = XmlUtils.toBean(BlogDetail.class, content.getBytes()).getBlog();
        fillUI();
        fillWebViewBody();
    }
    private void fillUI() {
        tvTitle.setText(mBlog.getTitle());
        tvSource.setText(mBlog.getAuthor());
        tvSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UIHelper.showUserCenter(getActivity(), mBlog.getAuthorId(),
//                        mBlog.getAuthor());
            }
        });
        tvTime.setText(StringUtils.friendly_time(mBlog.getPubDate()));
//       notifyFavorite(mBlog.getFavorite() == 1);
    }

    private void fillWebViewBody() {
        StringBuffer body = new StringBuffer();
        body.append(UIHelper.setHtmlCotentSupportImagePreview(mBlog.getBody()));
        body.append(UIHelper.WEB_STYLE).append(UIHelper.WEB_LOAD_IMAGES);
        webview.loadDataWithBaseURL(null, body.toString(), "text/html",
                "utf-8", null);

        try {
            System.out.println("save begin! :" + mBlogId);
            File file = new File(Environment.getExternalStorageDirectory(),
                    String.format("/OSChina/html/%d.html", mBlogId));
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(body.toString().getBytes());
            fos.close();
            System.out.println("save success! :" + file.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

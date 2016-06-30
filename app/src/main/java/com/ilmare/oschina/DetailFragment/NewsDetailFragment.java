package com.ilmare.oschina.DetailFragment;

import android.os.Environment;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.ilmare.oschina.Base.BaseFragment;
import com.ilmare.oschina.Beans.News;
import com.ilmare.oschina.Beans.NewsDetail;
import com.ilmare.oschina.Net.OSChinaApi;
import com.ilmare.oschina.R;
import com.ilmare.oschina.Utils.StringUtils;
import com.ilmare.oschina.Utils.UIHelper;
import com.ilmare.oschina.Utils.XmlUtils;
import com.ilmare.oschina.Widget.CustomerScrollView;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.InjectView;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：6/29/2016 2:26 PM
 * 版本号： 1.0
 * 版权所有(C) 6/29/2016
 * 描述：
 * ===============================
 */

public class NewsDetailFragment extends BaseFragment {


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

    private int mNewsId;  //新闻id
    private News mNews;   //要显示的新闻
    private int mCommentCount; //评论数目

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_detail;
    }


    @Override
    protected void init() {
        mCommentCount = getActivity().getIntent().getIntExtra("comment_count", 0);
        mNewsId = getActivity().getIntent().getIntExtra("news_id", 0);
        UIHelper.initWebView(webview);
    }

    @Override
    protected void initData() {
        OSChinaApi.getNewsDetail(mNewsId, mHandler);
    }


    @Override
    protected void onLoadSuccess(String content) {
        System.out.println(content);
        NewsDetail newsDetail = XmlUtils.toBean(NewsDetail.class, content.getBytes());
        System.out.println(newsDetail);
        mNews = newsDetail.getNews();

        fillUI();
        fillWebViewBody();
        //TODO
//        ((DetailActivity) getActivity()).setCommentCount(mNews
//                .getCommentCount());
    }


    private void fillUI() {
        tvTime.setText(mNews.getTitle());
        tvSource.setText(mNews.getAuthor());
        tvSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UIHelper.showUserCenter(getActivity(), mNews.getAuthorId(),
//                        mNews.getAuthor());
            }
        });

//        2015-9-29 17:04:31  -> 2个月前
        tvTime.setText(StringUtils.friendly_time(mNews.getPubDate()));

        //TODO
//        orite(mNews.getFavorite() == 1);
    }

    private void fillWebViewBody() {

        StringBuffer body = new StringBuffer();
        body.append(UIHelper.setHtmlCotentSupportImagePreview(mNews.getBody()));
        body.append(UIHelper.WEB_STYLE).append(UIHelper.WEB_LOAD_IMAGES);
        // 更多关于***软件的信息
        String softwareName = mNews.getSoftwareName();
        String softwareLink = mNews.getSoftwareLink();
        if (!StringUtils.isEmpty(softwareName)
                && !StringUtils.isEmpty(softwareLink))
            body.append(String
                    .format("<div id='oschina_software' style='margin-top:8px;color:#FF0000;font-weight:bold'>更多关于:&nbsp;<a href='%s'>%s</a>&nbsp;的详细信息</div>",
                            softwareLink, softwareName));
        // 相关新闻
        if (mNews != null && mNews.getRelatives() != null
                && mNews.getRelatives().size() > 0) {
            String strRelative = "";
            for (News.Relative relative : mNews.getRelatives()) {
                strRelative += String.format(
                        "<a href='%s' style='text-decoration:none'>%s</a><p/>",
                        relative.url, relative.title);
            }
            body.append("<p/><div style=\"height:1px;width:100%;background:#DADADA;margin-bottom:10px;\"/>"
                    + String.format("<br/> <b>相关资讯</b> <div><p/>%s</div>",
                    strRelative));
        }
        body.append("<br/>");

        try {
            System.out.println("save begin! :" + mNewsId);
            File file = new File(Environment.getExternalStorageDirectory(),
                    String.format("/OSChina/html/%d.html", mNewsId));
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(body.toString().getBytes());
            fos.close();
            System.out.println("save success! :" + file.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (webview != null) {
            webview.loadDataWithBaseURL(null, body.toString(), "text/html",
                    "utf-8", null);
        }

    }


}

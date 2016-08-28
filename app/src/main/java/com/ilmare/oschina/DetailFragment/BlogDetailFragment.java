package com.ilmare.oschina.DetailFragment;

import android.os.Environment;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.ilmare.oschina.Base.BaseFragment;
import com.ilmare.oschina.Beans.Blog;
import com.ilmare.oschina.Beans.BlogDetail;
import com.ilmare.oschina.Net.OSChinaApi;
import com.ilmare.oschina.R;
import com.ilmare.oschina.Utils.StringUtils;
import com.ilmare.oschina.Utils.UIHelper;
import com.ilmare.oschina.Utils.XmlUtils;

import java.io.File;
import java.io.FileOutputStream;

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

    private int mCommentCount;  //评论条数
    private int mBlogId;        //要显示的blog id
    private Blog mBlog;         //博客数据

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_detail;
    }


    @Override
    protected void init() {
        mCommentCount = getActivity().getIntent().getIntExtra("comment_count", 0);
        mBlogId = getActivity().getIntent().getIntExtra("blog_id", 0);

        //初始化webView-->设置字体大小 支持javascript 设置转向的处理
        UIHelper.initWebView(webview);

    }


    //请求网络
    @Override
    protected void initData() {


        OSChinaApi.getBlogDetail(mBlogId, mHandler);

    }


    /**
     * 从本地加载缓存的 html信息
     * @param mBlogId
     * @return
     */
    private boolean getBlogDetailFromLocal(int mBlogId) {
        File file = new File(Environment.getExternalStorageDirectory(),
                String.format("/OSChina/html/%d.html", mBlogId));

        if(file.exists()){
            webview.loadUrl(String.format("file:///sdcard/OSChina/html/%d.html", mBlogId));
            System.out.println("使用的缓存");
            return true;
        }
        return false;
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
                //TODO 转向用户中心
                //UIHelper.showUserCenter(getActivity(), mBlog.getAuthorId(),mBlog.getAuthor());
            }
        });
        tvTime.setText(StringUtils.friendly_time(mBlog.getPubDate()));
    }


    private void fillWebViewBody() {

        if(!getBlogDetailFromLocal(mBlogId)) { //如果读取本地失败 则加载

            StringBuffer body = new StringBuffer();

            //设置图片预览
            body.append(UIHelper.setHtmlCotentSupportImagePreview(mBlog.getBody()));

            //添加样式 添加加载图片
            body.append(UIHelper.WEB_STYLE).append(UIHelper.WEB_LOAD_IMAGES);

            //加载本地的html-->body.toString()
            webview.loadDataWithBaseURL(null, body.toString(), "text/html", "utf-8", null);

            //将html文件保存到本地
            saveHtmlToLocal(body);
        }

    }

    private void saveHtmlToLocal(StringBuffer body) {
        //将html文件保存到本地
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

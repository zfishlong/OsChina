package com.ilmare.oschina.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import com.ilmare.oschina.AppConfig;
import com.ilmare.oschina.AppContext;
import com.ilmare.oschina.Beans.News;
import com.ilmare.oschina.UI.DetailActivity;
import com.ilmare.oschina.UI.ImagePreviewActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class UIHelper {

    /**
     * 全局web样式
     */
    // 链接样式文件，代码块高亮的处理
    public final static String linkCss = "<script type=\"text/javascript\" src=\"file:///android_asset/shCore.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/brush.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/client.js\"></script>"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shThemeDefault.css\">"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shCore.css\">"
            + "<script type=\"text/javascript\">SyntaxHighlighter.all();</script>"
            + "<script type=\"text/javascript\">function showImagePreview(var url){window.location.url= url;}</script>";
    public final static String WEB_STYLE = linkCss
            + "<style>* {font-size:16px;line-height:20px;} p {color:#333;} a {color:#3E62A6;} img {max-width:310px;} "
            + "img.alignleft {float:left;max-width:120px;margin:0 10px 5px 0;border:1px solid #ccc;background:#fff;padding:2px;} "
            + "pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;overflow: auto;} "
            + "a.tag {font-size:15px;text-decoration:none;background-color:#cfc;color:#060;border-bottom:1px solid #B1D3EB;border-right:1px solid #B1D3EB;color:#3E6D8E;margin:2px 2px 2px 0;padding:2px 4px;white-space:nowrap;position:relative}</style>";

    public static final String WEB_LOAD_IMAGES = "<script type=\"text/javascript\"> var allImgUrls = getAllImgSrc(document.body.innerHTML);</script>";

    private static final String SHOWIMAGE = "ima-api:action=showImage&data=";


    /**
     * 显示新闻详情
     */
    public static void showNewsDetail(Context context, int newsId,
                                      int commentCount) {

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("news_id", newsId);
        intent.putExtra("comment_count", commentCount);

        intent.putExtra(DetailActivity.BUNDLE_KEY_DISPLAY_TYPE,
                DetailActivity.DISPLAY_NEWS);

        context.startActivity(intent);
    }


    //
    public static void showNewsRedirect(Context context, News news) {
        String url = news.getUrl();
        // 如果是活动则直接跳转活动详情页面
        String eventUrl = news.getNewType().getEventUrl();

        if (!StringUtils.isEmpty(eventUrl)) {
            showEventDetail(context,
                    StringUtils.toInt(news.getNewType().getAttachment()));
            return;
        }

        // url为空-旧方法
        if (StringUtils.isEmpty(url)) {
            int newsId = news.getId();
            int newsType = news.getNewType().getType();
            String objId = news.getNewType().getAttachment();
            switch (newsType) {
                case News.NEWSTYPE_NEWS:
                    showNewsDetail(context, newsId, news.getCommentCount());
                    break;
//            case News.NEWSTYPE_SOFTWARE:
//                showSoftwareDetail(context, objId);
//                break;
//            case News.NEWSTYPE_POST:
//                showPostDetail(context, StringUtils.toInt(objId),
//                        news.getCommentCount());
//                break;
//            case News.NEWSTYPE_BLOG:
//                showBlogDetail(context, StringUtils.toInt(objId),
//                        news.getCommentCount());
//                break;
                default:
                    break;
            }
        } else {
            showUrlRedirect(context, url);
        }
    }





    public static void initWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setDefaultFontSize(15);              //默认字体大小
        settings.setJavaScriptEnabled(true);          //javascript可用
        settings.setSupportZoom(true);                //缩放可用
        settings.setBuiltInZoomControls(true);
        int sysVersion = Build.VERSION.SDK_INT;       //去掉缩放按钮
        if (sysVersion >= 11) {
            settings.setDisplayZoomControls(false);
        } else {
            ZoomButtonsController zbc = new ZoomButtonsController(webView);
            zbc.getZoomControls().setVisibility(View.GONE);
        }
        webView.setWebViewClient(UIHelper.getWebViewClient());  //设置默认浏览器
    }

    /**
     * 获取webviewClient对象
     *
     * @return
     */
    public static WebViewClient getWebViewClient() {
        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                showUrlRedirect(view.getContext(), url);
                return true;
            }
        };
    }


    //
    private static void showUrlRedirect(Context context, String url) {
        if (url == null)
            return;
        //显示活动详细信息
        if (url.contains("city.oschina.net/")) {
            int id = StringUtils.toInt(url.substring(url.lastIndexOf('/') + 1));
            UIHelper.showEventDetail(context, id);
            return;
        }

        //显示图片
        if (url.startsWith(SHOWIMAGE)) {
            String realUrl = url.substring(SHOWIMAGE.length());
            try {
                JSONObject json = new JSONObject(realUrl);
                int idx = json.optInt("index");
                String[] urls = json.getString("urls").split(",");
                showImagePreview(context, idx, urls);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
        //
        URLsUtils urls = URLsUtils.parseURL(url);
        if (urls != null) {
            showLinkRedirect(context, urls.getObjType(), urls.getObjId(),
                    urls.getObjKey());
        } else {
//            openBrowser(context, url);
        }
    }

    //
    private static void showImagePreview(Context context, int index, String[] imageUrls) {
        ImagePreviewActivity.showImagePrivew(context, index, imageUrls);
    }

    private static void showLinkRedirect(Context context, int objType, int objId, String objKey) {
        //TODO 转去链接
        System.out.println("转去链接");
    }

    private static void showEventDetail(Context context, int i) {
        //TODO 显示活动
        System.out.println("显示活动");
    }


    public static String setHtmlCotentSupportImagePreview(String body) {
        // 读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
        if (AppContext.get(AppConfig.KEY_LOAD_IMAGE, true)|| TDevice.isWifiOpen()) {
            // 过滤掉 img标签的width,height属性
            body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
            body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
            // 添加点击图片放大支持
            // 添加点击图片放大支持
            body = body.replaceAll("(<img[^>]+src=\")(\\S+)\"",
                    "$1$2\" onClick=\"showImagePreview('$2')\"");
        } else {
            // 过滤掉 img标签
            body = body.replaceAll("<\\s*img\\s+([^>]*)\\s*>", "");
        }
        return body;
    }


    public static SpannableString parseActiveAction(int objecttype,
                                                    int objectcatalog, String objecttitle) {
        String title = "";
        int start = 0;
        int end = 0;
        if (objecttype == 32 && objectcatalog == 0) {
            title = "加入了开源中国";
        } else if (objecttype == 1 && objectcatalog == 0) {
            title = "添加了开源项目 " + objecttitle;
        } else if (objecttype == 2 && objectcatalog == 1) {
            title = "在讨论区提问：" + objecttitle;
        } else if (objecttype == 2 && objectcatalog == 2) {
            title = "发表了新话题：" + objecttitle;
        } else if (objecttype == 3 && objectcatalog == 0) {
            title = "发表了博客 " + objecttitle;
        } else if (objecttype == 4 && objectcatalog == 0) {
            title = "发表一篇新闻 " + objecttitle;
        } else if (objecttype == 5 && objectcatalog == 0) {
            title = "分享了一段代码 " + objecttitle;
        } else if (objecttype == 6 && objectcatalog == 0) {
            title = "发布了一个职位：" + objecttitle;
        } else if (objecttype == 16 && objectcatalog == 0) {
            title = "在新闻 " + objecttitle + " 发表评论";
        } else if (objecttype == 17 && objectcatalog == 1) {
            title = "回答了问题：" + objecttitle;
        } else if (objecttype == 17 && objectcatalog == 2) {
            title = "回复了话题：" + objecttitle;
        } else if (objecttype == 17 && objectcatalog == 3) {
            title = "在 " + objecttitle + " 对回帖发表评论";
        } else if (objecttype == 18 && objectcatalog == 0) {
            title = "在博客 " + objecttitle + " 发表评论";
        } else if (objecttype == 19 && objectcatalog == 0) {
            title = "在代码 " + objecttitle + " 发表评论";
        } else if (objecttype == 20 && objectcatalog == 0) {
            title = "在职位 " + objecttitle + " 发表评论";
        } else if (objecttype == 101 && objectcatalog == 0) {
            title = "回复了动态：" + objecttitle;
        } else if (objecttype == 100) {
            title = "更新了动态";
        }
        SpannableString sp = new SpannableString(title);
        // 设置标题字体大小、高亮
        if (!StringUtils.isEmpty(objecttitle)) {
            start = title.indexOf(objecttitle);
            if (objecttitle.length() > 0 && start > 0) {
                end = start + objecttitle.length();
                sp.setSpan(new AbsoluteSizeSpan(14, true), start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sp.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#0e5986")),
                        start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return sp;
    }

    /**
     * 组合动态的回复文本
     *
     * @param name
     * @param body
     * @return
     */
    public static SpannableStringBuilder parseActiveReply(String name,
                                                          String body) {
        Spanned span = Html.fromHtml(body.trim());
        SpannableStringBuilder sp = new SpannableStringBuilder(name + "：");
        sp.append(span);
        // 设置用户名字体加粗、高亮
        // sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
        // name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#576B95")), 0,
                name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sp;
    }


}

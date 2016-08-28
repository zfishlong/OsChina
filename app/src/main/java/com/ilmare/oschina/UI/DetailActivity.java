package com.ilmare.oschina.UI;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ilmare.oschina.Base.BaseActivity;
import com.ilmare.oschina.Base.BaseFragment;
import com.ilmare.oschina.DetailFragment.BlogDetailFragment;
import com.ilmare.oschina.DetailFragment.NewsDetailFragment;
import com.ilmare.oschina.DetailFragment.TweetDetailFragment;
import com.ilmare.oschina.DetailFragment.EventDetailFragment;
import com.ilmare.oschina.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：6/22/2016 5:30 PM
 * 版本号： 1.0
 * 版权所有(C) 6/22/2016
 * 描述：信息详情
 * ===============================
 */
public class DetailActivity extends BaseActivity {

    public static final String BUNDLE_KEY_DISPLAY_TYPE = "display_type";  //显示的类型
    public static final int DISPLAY_NEWS = 0;  //显示资讯
    public static final int DISPLAY_EVENT =1;  //显示活动
    public static final int DISPLAY_BLOG =3;   //显示博客详情
    public static final int DISPLAY_TWEET =4 ; //显示弹

    @InjectView(R.id.container)
    FrameLayout container;
    @InjectView(R.id.emoji_keyboard)
    FrameLayout emojiKeyboard;
    @InjectView(R.id.activity_root)
    RelativeLayout activityRoot;


    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private BaseFragment toShowFragment; //要显示的fragment

    private int actionBarTitle=R.string.actionbar_title_detail; //标题

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected int getActionBarTitle() {
        return actionBarTitle;
    }

    //是否有自定义的背景
    @Override
    protected boolean hasBackButton() {
        return true;
    }

    //自定义view的布局id
    @Override
    protected int getActionBarCustomView() {
        return R.layout.actionbar_custom_backtitle;
    }

    //界面布局的id
    @Override
    public int getLayoutId() {
        return R.layout.activity_detail;
    }



    //初始化
    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.inject(this);
        int displayType = getIntent().getIntExtra(BUNDLE_KEY_DISPLAY_TYPE,DISPLAY_NEWS); //默认是新闻类型

        switch (displayType) {
            case DISPLAY_NEWS:   //显示新闻
                actionBarTitle=R.string.actionbar_title_detail;
                toShowFragment=new NewsDetailFragment();
                break;


            case DISPLAY_EVENT:   //显示活動
                actionBarTitle=R.string.actionbar_title_event_detail;
                toShowFragment=new EventDetailFragment();
                break;


            case DISPLAY_BLOG:   //显示博客
                actionBarTitle=R.string.actionbar_title_detail;
                toShowFragment=new BlogDetailFragment();
                break;


            case DISPLAY_TWEET: //显示动弹详情
                actionBarTitle = R.string.actionbar_title_tweet;
                toShowFragment = new TweetDetailFragment();
                break;


        }

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,toShowFragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}

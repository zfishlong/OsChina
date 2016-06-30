package com.ilmare.oschina;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ilmare.oschina.Base.BaseActivity;
import com.ilmare.oschina.Base.BaseFragment;
import com.ilmare.oschina.DetailFragment.NewsDetailFragment;
import com.ilmare.oschina.Fragment.DefaultFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：6/22/2016 5:30 PM
 * 版本号： 1.0
 * 版权所有(C) 6/22/2016
 * 描述：
 * ===============================
 */
public class DetailActivity extends BaseActivity {

    public static final String BUNDLE_KEY_DISPLAY_TYPE = "display_type";
    public static final int DISPLAY_NEWS = 0;

    @InjectView(R.id.container)
    FrameLayout container;
    @InjectView(R.id.emoji_keyboard)
    FrameLayout emojiKeyboard;
    @InjectView(R.id.activity_root)
    RelativeLayout activityRoot;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private BaseFragment toShowFragment; //要显示的fragment

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.actionbar_title_detail;
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
        int displayType = getIntent().getIntExtra(BUNDLE_KEY_DISPLAY_TYPE,
                DISPLAY_NEWS);
        switch (displayType) {
            case DISPLAY_NEWS:   //
                toShowFragment=new NewsDetailFragment();
                break;
        }

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,toShowFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}

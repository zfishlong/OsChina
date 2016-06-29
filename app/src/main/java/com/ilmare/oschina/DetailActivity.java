package com.ilmare.oschina;

import com.ilmare.oschina.Base.BaseActivity;

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
    public static final int DISPLAY_NEWS =0 ;


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

    @Override
    protected int getActionBarCustomView() {
        return R.layout.actionbar_custom_backtitle;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }



}

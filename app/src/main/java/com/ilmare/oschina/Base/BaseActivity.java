package com.ilmare.oschina.Base;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ilmare.oschina.R;
import com.ilmare.oschina.UI.AppManager;
import com.ilmare.oschina.Utils.StringUtils;
import com.ilmare.oschina.Utils.TDevice;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：6/29/2016 8:41 AM
 * 版本号： 1.0
 * 版权所有(C) 6/29/2016
 * 描述：
 * ===============================
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ActionBar supportActionBar;
    private TextView mTvActionTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        AppManager.getAppManager().addActivity(this);

        //初始化操作
        init(savedInstanceState);

        //初始化Actionbar
        if (hasActionBar()) {
            supportActionBar = getSupportActionBar();
            //初始化actionbar
            initActionBar(supportActionBar);
        }


        //初始化View
        initView();
        //初始化数据
        initData();
    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void init(Bundle savedInstanceState);

    protected abstract boolean hasActionBar();

    protected void initActionBar(ActionBar actionBar) {
        if (actionBar == null)
            return;
        if (hasBackButton()) {
            // 让ActionBar自定义内容
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            int layoutRes = getActionBarCustomView();

            // ---------------------- 创建自定义布局 ↓
            View view = View.inflate(this,layoutRes == 0 ? R.layout.actionbar_custom_backtitle
                    : layoutRes,null);
            View back = view.findViewById(R.id.btn_back);
            if (back == null) {
                throw new IllegalArgumentException(
                        "can not find R.id.btn_back in customView");
            }


            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TDevice.hideSoftKeyboard(getCurrentFocus()); // 隐藏软键盘
                    onBackPressed(); // 按下了返回键
                }
            });

            //
            mTvActionTitle = (TextView) view.findViewById(R.id.tv_actionbar_title);

            if (mTvActionTitle == null) {
                throw new IllegalArgumentException(
                        "can not find R.id.tv_actionbar_title in customView");
            }

            int titleRes = getActionBarTitle();
            if (titleRes != 0) {
                mTvActionTitle.setText(titleRes);
            }

            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                   LayoutParams.MATCH_PARENT);

            // 创建自定义布局 ↑
            // 设置自定义内容
            actionBar.setCustomView(view, params);

            View spinner = actionBar.getCustomView().findViewById(R.id.spinner);
            if (haveSpinner()) {
                spinner.setVisibility(View.VISIBLE);
            } else {
                spinner.setVisibility(View.GONE);
            }
        } else {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setDisplayUseLogoEnabled(false);
            int titleRes = getActionBarTitle();
            if (titleRes != 0) {
                actionBar.setTitle(titleRes);
            }
        }
    }


    public void setActionBarTitle(int resId) {
        if (resId != 0) {
            setActionBarTitle(getString(resId));
        }
    }

    public void setActionBarTitle(String title) {
        if (StringUtils.isEmpty(title)) {
            title = getString(R.string.app_name);
        }
        if (hasActionBar() && supportActionBar != null) {
            if (mTvActionTitle != null) {
                mTvActionTitle.setText(title);
            }
            supportActionBar.setTitle(title);
        }
    }

    private boolean haveSpinner() {
        return false;
    }

    protected int getActionBarCustomView() {
        return 0;
    }

    protected boolean hasBackButton() {
        return false;
    }

    protected abstract int getActionBarTitle();


    public abstract int getLayoutId();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        TDevice.hideSoftKeyboard(getCurrentFocus());
        AppManager.getAppManager().finishActivity(this);
    }
}

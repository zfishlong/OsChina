package com.ilmare.oschina;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.ilmare.oschina.UI.MainTab;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    @InjectView(R.id.realtabcontent)
    FrameLayout realtabcontent;
    @InjectView(android.R.id.tabhost)
    FragmentTabHost tabhost;
    @InjectView(R.id.quick_option_iv)
    ImageView quickOptionIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        initView();
    }

    private void initView() {

        // 初始化底部FragmentTabHost
        tabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        if (android.os.Build.VERSION.SDK_INT > 10) {
            tabhost.getTabWidget().setShowDividers(0);
        }

        initTabs();



    }

    private void initTabs() {
        MainTab[] tabs = MainTab.values();
        final int size = tabs.length;

        for (int i = 0; i < size; i++) {
            // 找到每一个枚举的Fragment对象
            MainTab mainTab = tabs[i];

            // 1. 创建一个新的选项卡
            TabHost.TabSpec tab = tabhost.newTabSpec(getString(mainTab.getResName()));

            // -------------自定义选项卡 ↓
            View indicator = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.tab_indicator, null);
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            Drawable drawable = this.getResources().getDrawable(mainTab.getResIcon());
            title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null,null);

            if (i == 2) {
                indicator.setVisibility(View.INVISIBLE);
            }

            title.setText(getString(mainTab.getResName()));
            tab.setIndicator(indicator);


//            方法二：
//            tab.setContent(new TabContentFactory() {
//            	@Override
//            	public View createTabContent(String tag) {
//            		return new View(MainActivity.this);
//            	}
//            });



            Bundle bundle = new Bundle();
            bundle.putString("key", "content: " + getString(mainTab.getResName()));

            // 2. 把新的选项卡添加到TabHost中
            tabhost.addTab(tab, mainTab.getClz(), bundle);

            tabhost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


}

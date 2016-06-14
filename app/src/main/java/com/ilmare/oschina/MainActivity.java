package com.ilmare.oschina;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toolbar;

import com.ilmare.oschina.UI.MainTab;
import com.ilmare.oschina.Utils.ToastUtil;

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

        restoreActionBar();


//        ViewPager viewPager
//        viewPager.setOffscreenPageLimit();


        //1.解析bean对象
//        DefaultHttpClient httpClient = new DefaultHttpClient();
//        try {
//			HttpResponse response = httpClient.execute(new HttpGet("http://192.168.1.100:8080/oschina/list/news/page0.xml"));
//			if(response.getStatusLine().getStatusCode()==200){
//				InputStream is = response.getEntity().getContent();
//				NewsList newsList = XmlUtils.toBean(NewsList.class, is);
//				List<News> list = newsList.getList();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

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

            //给textView设置坐上右下的图标
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

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        // 设置显示为标准模式, 还有NAVIGATION_MODE_LIST列表模式, NAVIGATION_MODE_TABS选项卡模式. 参见ApiDemos
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        // 设置显示标题
        actionBar.setDisplayShowTitleEnabled(true);
        // 设置标题
        actionBar.setTitle(getTitle());

//        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        restoreActionBar();
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
//            restoreActionBar();
//            return true;
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search:
                ToastUtil.showToast(this, "Search");
                break;
            case R.id.share:
                ToastUtil.showToast(this, "share");
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


}

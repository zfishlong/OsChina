package com.ilmare.oschina.UI;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
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

import com.ilmare.oschina.Fragment.DrawerFragment;
import com.ilmare.oschina.R;
import com.ilmare.oschina.Utils.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity  implements View.OnTouchListener, DrawerLayout.DrawerListener, DrawerFragment.OnDrawerItemSelectedListener, View.OnClickListener {

    @InjectView(R.id.realtabcontent)
    FrameLayout realtabcontent;

    @InjectView(android.R.id.tabhost)
    FragmentTabHost tabhost;
    @InjectView(R.id.quick_option_iv)
    ImageView quickOptionIv;
    @InjectView(R.id.drawerLayout)
    DrawerLayout drawerLayout;


    DrawerFragment navigationDrawer;
    ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        //布局文件中的fragment要用findFragmentById 替代
        navigationDrawer= (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        initView();
    }

    private void initView() {
        // 初始化底部FragmentTabHost  使tabhost与fragement关联
        tabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        //去掉分割线
        if (Build.VERSION.SDK_INT > 10) {
            tabhost.getTabWidget().setShowDividers(0);
        }

        initTabs();


        restoreActionBar();
        quickOptionIv.setOnClickListener(this);
    }

    private void initTabs() {
        MainTab[] tabs = MainTab.values();
        final int size = tabs.length;

        for (int i = 0; i < size; i++) {
            // 找到每一个枚举的Fragment对象
            MainTab mainTab = tabs[i];

            // 1. 创建一个新的选项卡
            TabHost.TabSpec tab = tabhost.newTabSpec(getString(mainTab.getResName()));

            //自定义选项卡 方法一：
            View indicator = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.tab_indicator, null);
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            Drawable drawable = this.getResources().getDrawable(mainTab.getResIcon());

            //给textView设置坐上右下的图标
            title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);

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
        actionBar.setDisplayHomeAsUpEnabled(true);//设置显示左侧按钮
        actionBar.setHomeButtonEnabled(true);//设置左侧按钮可点
        // 设置标题
        actionBar.setTitle(getTitle());


        navigationDrawer.setOnDrawerItemSelectedListener(this);
        drawerLayout.addDrawerListener(this);
        //初始化开关，并和drawer关联
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerToggle.syncState();//该方法会自动和actionBar关联

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        restoreActionBar();
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            restoreActionBar();
            return true;
        }
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
                ToastUtil.showToast(this, "Share");
                break;
        }
        return super.onOptionsItemSelected(item)||drawerToggle.onOptionsItemSelected(item);
    }




    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    /**
     * Drawer的回调方法，需要在该方法中对Toggle做对应的操作
     */
    @Override
    public void onDrawerOpened(View drawerView) {// 打开drawer
        drawerToggle.onDrawerOpened(drawerView);//需要把开关也变为打开
        invalidateOptionsMenu();
    }

    @Override
    public void onDrawerClosed(View drawerView) {// 关闭drawer
        drawerToggle.onDrawerClosed(drawerView);//需要把开关也变为关闭
        invalidateOptionsMenu();
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {// drawer滑动的回调
        drawerToggle.onDrawerSlide(drawerView, slideOffset);
    }

    @Override
    public void onDrawerStateChanged(int newState) {// drawer状态改变的回调
        drawerToggle.onDrawerStateChanged(newState);
    }


    @Override
    public void onDrawerItemSelected(int position) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (position) {
            case R.id.menu_item_quests: //技术问答
                ToastUtil.showToast(this,"技术问答");
                break;
            case R.id.menu_item_opensoft: //开源软件
                ToastUtil.showToast(this,"开源软件");
                break;
            case R.id.menu_item_blog:  //博客
                ToastUtil.showToast(this,"博客");
                break;
            case R.id.menu_item_gitapp: //git客户端
                ToastUtil.showToast(this,"git客户端");
                break;
            case R.id.menu_item_rss:    //
                break;
            case R.id.menu_item_setting://设置
                ToastUtil.showToast(this,"设置");
                break;
            case R.id.menu_item_exit:   //退出
                ToastUtil.showToast(this,"退出");
                break;
        }
    }

    //快捷菜单点击事件
    @Override
    public void onClick(View v) {
        showQuickOption();
    }

    // 显示快速操作界面
    private void showQuickOption() {
        final QuickOptionDialog dialog = new QuickOptionDialog(
                MainActivity.this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

}

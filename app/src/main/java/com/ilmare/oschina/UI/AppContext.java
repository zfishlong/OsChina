package com.ilmare.oschina.UI;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.ilmare.oschina.Base.BaseApplication;
import com.ilmare.oschina.Beans.User;
import com.ilmare.oschina.Net.ApiHttpClient;
import com.ilmare.oschina.Utils.CyptoUtils;
import com.ilmare.oschina.Utils.StringUtils;
import com.ilmare.oschina.Utils.TLog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import org.kymjs.kjframe.bitmap.BitmapConfig;
import org.kymjs.kjframe.utils.KJLoger;

import java.util.Properties;
import java.util.UUID;
/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 */
public class AppContext extends BaseApplication {

    public static final int PAGE_SIZE = 20;// 默认分页大小
    private static AppContext instance;
    private int loginUid;
    private boolean login;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
        initLogin();

        // Thread.setDefaultUncaughtExceptionHandler(AppException
        // .getAppExceptionHandler(this));
    }

    private void init() {
        // 初始化网络请求
        AsyncHttpClient client = new AsyncHttpClient();
        // KGBJVUYIBHS2345UIY 令牌
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);

        ApiHttpClient.setHttpClient(client);
        ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));
        // Log控制器
        KJLoger.openDebutLog(true);
        TLog.DEBUG =true;
        // Bitmap缓存地址
        BitmapConfig.CACHEPATH = "OSChina/imagecache";
    }

    private void initLogin() {
        User user = getLoginUser();
        if (null != user && user.getId() > 0) {
            login = true;
            loginUid = user.getId();
        } else {
            this.cleanLoginInfo();
        }
    }

    /**
     * 获得当前app运行的AppContext
     * 
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    /**
     * 获取cookie时传AppConfig.CONF_COOKIE
     * 
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res;
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    /**
     * 获取App唯一标识
     * 
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 获取App安装包信息
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 保存登录信息
     */
    @SuppressWarnings("serial")
    public void saveUserInfo(final User user) {
        this.loginUid = user.getId();
        this.login = true;
        setProperties(new Properties() {
            {
                setProperty("user.uid", String.valueOf(user.getId()));
                setProperty("user.name", user.getName());
                setProperty("user.face", user.getPortrait());// 用户头像-文件名
                setProperty("user.account", user.getAccount());
                setProperty("user.pwd",
                        CyptoUtils.encode("oschinaApp", user.getPwd()));
                setProperty("user.location", user.getLocation());
                setProperty("user.followers",
                        String.valueOf(user.getFollowers()));
                setProperty("user.fans", String.valueOf(user.getFans()));
                setProperty("user.score", String.valueOf(user.getScore()));
                setProperty("user.favoritecount",
                        String.valueOf(user.getFavoritecount()));
                setProperty("user.gender", String.valueOf(user.getGender()));
                setProperty("user.isRememberMe",
                        String.valueOf(user.isRememberMe()));// 是否记住我的信息
            }
        });
    }

    /**
     * 更新用户信息
     * 
     * @param user
     */
    @SuppressWarnings("serial")
    public void updateUserInfo(final User user) {
        setProperties(new Properties() {
            {
                setProperty("user.name", user.getName());
                setProperty("user.face", user.getPortrait());// 用户头像-文件名
                setProperty("user.followers",
                        String.valueOf(user.getFollowers()));
                setProperty("user.fans", String.valueOf(user.getFans()));
                setProperty("user.score", String.valueOf(user.getScore()));
                setProperty("user.favoritecount",
                        String.valueOf(user.getFavoritecount()));
                setProperty("user.gender", String.valueOf(user.getGender()));
            }
        });
    }

    /**
     * 获得登录用户的信息
     * 
     * @return
     */
    public User getLoginUser() {
        User user = new User();
        user.setId(StringUtils.toInt(getProperty("user.uid"), 0));
        user.setName(getProperty("user.name"));
        user.setPortrait(getProperty("user.face"));
        user.setAccount(getProperty("user.account"));
        user.setLocation(getProperty("user.location"));
        user.setFollowers(StringUtils.toInt(getProperty("user.followers"), 0));
        user.setFans(StringUtils.toInt(getProperty("user.fans"), 0));
        user.setScore(StringUtils.toInt(getProperty("user.score"), 0));
        user.setFavoritecount(StringUtils.toInt(
                getProperty("user.favoritecount"), 0));
        user.setRememberMe(StringUtils.toBool(getProperty("user.isRememberMe")));
        user.setGender(getProperty("user.gender"));
        return user;
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = 0;
        this.login = false;
        removeProperty("user.uid", "user.name", "user.face", "user.location",
                "user.followers", "user.fans", "user.score",
                "user.isRememberMe", "user.gender", "user.favoritecount");
    }

    public int getLoginUid() {
        return loginUid;
    }

    public boolean isLogin() {
//    	return true;
        return login;
    }

    /**
     * 用户注销
     */
    public void Logout() {
//        cleanLoginInfo();
//        ApiHttpClient.cleanCookie();
//        this.cleanCookie();
//        this.login = false;
//        this.loginUid = 0;
//
//        Intent intent = new Intent(Constants.INTENT_ACTION_LOGOUT);
//        sendBroadcast(intent);
    }

    /**
     * 清除保存的缓存
     */
    public void cleanCookie() {
        removeProperty(AppConfig.CONF_COOKIE);
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
//        DataCleanManager.cleanDatabases(this);
//        // 清除数据缓存
//        DataCleanManager.cleanInternalCache(this);
//        // 2.2版本才有将应用缓存转移到sd卡的功能
//        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
//            DataCleanManager.cleanCustomCache(MethodsCompat
//                    .getExternalCacheDir(this));
//        }
//        // 清除编辑器保存的临时内容
//        Properties props = getProperties();
//        for (Object key : props.keySet()) {
//            String _key = key.toString();
//            if (_key.startsWith("temp"))
//                removeProperty(_key);
//        }
//        new KJBitmap().cleanCache();
    }

    public static void setLoadImage(boolean flag) {
//        set(KEY_LOAD_IMAGE, flag);
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     * 
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    public static String getTweetDraft() {
//        return getPreferences().getString(
//                KEY_TWEET_DRAFT + getInstance().getLoginUid(), "");
        return null;
    }

    public static void setTweetDraft(String draft) {
//        set(KEY_TWEET_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static String getNoteDraft() {
        return getPreferences().getString(
                AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setNoteDraft(String draft) {
        set(AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static boolean isFirstStart() {
//        return getPreferences().getBoolean(KEY_FIRST_START, true);
        return false;
    }

    public static void setFirstStart(boolean frist) {
//        set(KEY_FIRST_START, frist);
    }
}

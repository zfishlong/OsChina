package com.ilmare.oschina.Adapter;

import android.os.Bundle;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：6/15/2016 11:33 PM
 * 版本号： 1.0
 * 版权所有(C) 6/15/2016
 * 描述：
 * ===============================
 */

public class FragmentInfo {
    private Class<?> clazz;
    private Bundle bundle;

    public FragmentInfo(Class<?> clazz, Bundle bundle) {
        super();
        this.clazz = clazz;
        this.bundle = bundle;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
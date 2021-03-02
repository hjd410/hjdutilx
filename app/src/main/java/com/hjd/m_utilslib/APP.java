package com.hjd.m_utilslib;

import android.app.Application;

import com.hjd.apputils.app.MyLib;

/**
 * Created by HJD on 2021/1/4 0004 and 16:22.
 */
public class APP extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        MyLib.getInstance().init(this);
    }

}

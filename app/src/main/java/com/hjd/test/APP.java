package com.hjd.test;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hjd.apputils.app.MyLib;

/**
 * Created by HJD on 2021/1/4 0004 and 16:22.
 */
public class APP extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        MyLib.getInstance().init(this);
        ARouter.openDebug();
        ARouter.openLog();
        ARouter.init(this);

    }
}

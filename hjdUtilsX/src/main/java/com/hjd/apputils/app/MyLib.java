package com.hjd.apputils.app;

import android.app.Application;
import android.content.Context;


/**
 * Created by HJD on 2021/1/4 0004 and 9:57.
 */
public class MyLib {

    Application context;

    private MyLib() {

    }

    public static MyLib getInstance() {
        return Utils.lib;
    }

    private static class Utils {
        private static MyLib lib = new MyLib();
    }

    public MyLib init(Application application) {
        context = application;
//        Logger.addLogAdapter(new AndroidLogAdapter());
        return this;
    }

    public Context getContext() {
        return context;
    }


}

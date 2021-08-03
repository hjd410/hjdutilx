package com.hjd.apputils.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.hjd.apputils.app.MyLib;

import java.util.Stack;

/**
 * Created by HJD on 2019/2/28/0028 and 9:05.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 * Live beautifully,dream passionately,love completely.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 * Activity 管理类
 */
public class AppManager {
    private volatile static AppManager instance = null;
    private static Stack<Activity> hashSet = new Stack<>();

    private AppManager() {

    }

    public static AppManager getInstance() {
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null) {
                    instance = new AppManager();
                }
            }
        }
        return instance;
    }

    /**
     * 每一个Activity 在 onCreate 方法的时候，可以装入当前this
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        try {
            hashSet.add(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeActivity(Activity activity) {
        hashSet.remove(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            hashSet.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < hashSet.size(); i++) {
            if (null != hashSet.get(i)) {
                hashSet.get(i).finish();
            }
        }
        hashSet.clear();
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : hashSet) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
            @SuppressLint("ServiceCast")
            ActivityManager activityMgr = (ActivityManager) MyLib.getInstance().getContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(MyLib.getInstance().getContext().getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    /**
     * 调用此方法用于销毁所有的Activity，然后我们在调用此方法之前，调到登录的Activity
     */
    public void exit() {
        try {
            for (Activity activity : hashSet) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

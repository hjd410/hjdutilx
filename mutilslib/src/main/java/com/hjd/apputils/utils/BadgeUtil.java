package com.hjd.apputils.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.hjd.apputils.app.MyLib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by HJD on 2019/1/16 and 10:43.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 * Live beautifully,dream passionately,love completely.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 */
public final class BadgeUtil {

    private BadgeUtil() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

    /**
     * 设置Badge 目前支持Launcher
     */
    public static void setBadgeCount(int count, int iconResId) {
        if (count <= 0) {
            count = -1;
        } else {
            count = Math.max(0, Math.min(count, 99));
        }

        if (Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {
            setBadgeOfMIUI(count, iconResId);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("sony")) {
            setBadgeOfSony(count);
        } else if (Build.MANUFACTURER.toLowerCase().contains("samsung") ||
                Build.MANUFACTURER.toLowerCase().contains("lg")) {
            setBadgeOfSumsung(count);
        } else if (Build.MANUFACTURER.toLowerCase().contains("htc")) {
            setBadgeOfHTC(count);
        } else if (Build.MANUFACTURER.toLowerCase().contains("nova")) {
            setBadgeOfNova(count);
        } else if (Build.MANUFACTURER.toLowerCase().contains("huawei")) {
            setBadgeHuawei(count);
        } else if (Build.MANUFACTURER.toLowerCase().contains("oppo")) {
            setBadgeNumberOPPO(count);
        } else if (Build.MANUFACTURER.toLowerCase().contains("vivo")) {

        } else {
//            Toast.makeText(context, "Not Found Support Launcher", Toast.LENGTH_LONG).show();

        }
    }

    /**
     * 设置MIUI的Badge
     */
    private static void setBadgeOfMIUI(int count, int iconResId) {
        NotificationManager mNotificationManager = (NotificationManager) MyLib.getInstance().getContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyLib.getInstance().getContext());
        builder.setContentTitle("标题").setContentText("消息正文").setSmallIcon(iconResId);
        Notification notification = builder.build();
        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mNotificationManager.notify(0, notification);
    }

    /**
     * 设置索尼的Badge
     * 需添加权限：<uses-permission android:name="com.sonyericsson.home.permission.BROADCAST_BADGE" />
     */
    private static void setBadgeOfSony(int count) {
        String launcherClassName = getLauncherClassName();
        if (launcherClassName == null) {
            return;
        }
        boolean isShow = true;
        if (count == 0) {
            isShow = false;
        }
        Intent localIntent = new Intent();
        localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow);//是否显示
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", launcherClassName);//启动页
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(count));//数字
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", MyLib.getInstance().getContext().getPackageName());//包名
        MyLib.getInstance().getContext().sendBroadcast(localIntent);
    }

    /**
     * 设置三星的Badge\设置LG的Badge
     */
    private static void setBadgeOfSumsung(int count) {
        // 获取你当前的应用
        String launcherClassName = getLauncherClassName();
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", MyLib.getInstance().getContext().getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        MyLib.getInstance().getContext().sendBroadcast(intent);
    }

    /**
     * 华为的 设置角标
     */
    private static void setBadgeHuawei(int num) {
        try {
            Bundle bunlde = new Bundle();
            bunlde.putString("package", "com.sy.app");
            bunlde.putString("class", "com.sy.app.common.activity.SplashActivity");
//            bunlde.putString("class", "com.sy.app.main.MainActivity");
            bunlde.putInt("badgenumber", num);
            MyLib.getInstance().getContext().getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bunlde);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置HTC的Badge
     */
    private static void setBadgeOfHTC(int count) {
        Intent intentNotification = new Intent("com.htc.launcher.action.SET_NOTIFICATION");
        ComponentName localComponentName = new ComponentName(MyLib.getInstance().getContext().getPackageName(), getLauncherClassName());
        intentNotification.putExtra("com.htc.launcher.extra.COMPONENT", localComponentName.flattenToShortString());
        intentNotification.putExtra("com.htc.launcher.extra.COUNT", count);
        MyLib.getInstance().getContext().sendBroadcast(intentNotification);

        Intent intentShortcut = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
        intentShortcut.putExtra("packagename", MyLib.getInstance().getContext().getPackageName());
        intentShortcut.putExtra("count", count);
        MyLib.getInstance().getContext().sendBroadcast(intentShortcut);
    }

    /**
     * 设置Nova的Badge
     */
    private static void setBadgeOfNova(int count) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("tag", MyLib.getInstance().getContext().getPackageName() + "/" + getLauncherClassName());
        contentValues.put("count", count);
        MyLib.getInstance().getContext().getContentResolver().insert(Uri.parse("content://com.teslacoilsw.notifier/unread_count"),
                contentValues);
    }

    public static void setBadgeOfMadMode(int count, String packageName, String className) {
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", packageName);
        intent.putExtra("badge_count_class_name", className);
        MyLib.getInstance().getContext().sendBroadcast(intent);
    }

    /**
     * oppo手机角标
     *
     * @param number 数量
     */
    public static void setBadgeNumberOPPO(int number) {
        try {
            if (number == 0) {
                number = -1;
            }
            Intent intent = new Intent("com.oppo.unsettledevent");
            intent.putExtra("pakeageName", MyLib.getInstance().getContext().getPackageName());
            intent.putExtra("number", number);
            intent.putExtra("upgradeNumber", number);
            if (canResolveBroadcast(intent)) {
                MyLib.getInstance().getContext().sendBroadcast(intent);
            } else {
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("app_badge_count", number);
                    MyLib.getInstance().getContext().getContentResolver().call(Uri.parse("content://com.android.badge/badge"), "setAppBadgeCount", null, extras);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean canResolveBroadcast(Intent intent) {
        PackageManager packageManager = MyLib.getInstance().getContext().getPackageManager();
        List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(intent, 0);
        return receivers != null && receivers.size() > 0;
    }

    /**
     * VIVO手机角标
     *
     * @param number 数量
     */
    public static void setBadgeNumberVIVO(int number) {
        try {
            Intent intent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
            intent.putExtra("packageName", MyLib.getInstance().getContext().getPackageName());
            String launchClassName = MyLib.getInstance().getContext().getPackageManager().getLaunchIntentForPackage(MyLib.getInstance().getContext().getPackageName()).getComponent().getClassName();
            intent.putExtra("className", launchClassName);
            intent.putExtra("notificationNum", number);
            MyLib.getInstance().getContext().sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 重置Badge
     */
    public static void resetBadgeCount(int iconResId) {
        setBadgeCount(0, iconResId);
    }

    public static String getLauncherClassName() {
        PackageManager packageManager = MyLib.getInstance().getContext().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setPackage(MyLib.getInstance().getContext().getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }
        return info.activityInfo.name;
    }
}

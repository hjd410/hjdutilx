package com.hjd.apputils.utils;


import org.greenrobot.eventbus.EventBus;

/**
 * Created by HJD on 2018/7/30 0030 and 22:22.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 * Live beautifully,dream passionately,love completely.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 */
public class EventUtil {

    private String msg;
    private int type;

    public EventUtil(String msg, int type) {
        this.msg = msg;
        this.type = type;
    }

    public int getType ( ) {
        return type;
    }

    public void setType (int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    //    @Subscribe
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    //    @Subscribe
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void sendEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    public static void sendStickyEvent(Event event) {
        EventBus.getDefault().postSticky(event);
    }
}

package com.hjd.apputils.network;

/**
 * Created by HJD on 2019/7/16 and 10:56.
 * -=-=暗物质=-=-
 */
public class Urls {
    private Urls() {
    }

    public static Urls getInstance() {
        return UrlsList.URLS;
    }

    private static class UrlsList {
        private static final Urls URLS = new Urls();

    }

    private static  String serverUrl = "";

    public static  String login = serverUrl + "";


}

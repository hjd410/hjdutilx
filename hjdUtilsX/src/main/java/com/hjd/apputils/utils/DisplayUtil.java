package com.hjd.apputils.utils;

import com.hjd.apputils.app.MyLib;

/**
 * Created by HJD on 2018/7/25 0025 and 20:08.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 * Live beautifully,dream passionately,love completely.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 * 适配
 */
public class DisplayUtil {
    /**
     * 将px装换成dp，保证尺寸不变
     *
     * @param pxValue 传入的数值 float
     * @return
     */
    public static int px2dp(float pxValue) {
        float density = MyLib.getInstance().getContext().getResources().getDisplayMetrics().density;//得到设备的密度
        return (int) (pxValue / density + 0.5f);
    }

    /**
     * dp转px
     *
     * @param dpValue 传入的数值 float
     * @return
     */
    public static int dp2px(float dpValue) {
        float density = MyLib.getInstance().getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    /**
     * px转换成sp
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        float scaleDensity = MyLib.getInstance().getContext().getResources().getDisplayMetrics().scaledDensity;//缩放密度
        return (int) (pxValue / scaleDensity + 0.5f);
    }

    /**
     * sp转换成px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        float scaleDensity = MyLib.getInstance().getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scaleDensity + 0.5f);
    }
}

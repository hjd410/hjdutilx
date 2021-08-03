package com.hjd.apputils.utils;

import com.hjd.apputils.app.MyLib;

/**
 * Created by 大灯泡 on 2016/1/16.
 */
public class DimensUtils {
    /**
     * dip转px
     */
    public static int dipToPx(float dip) {
        return (int) (dip * MyLib.getInstance().getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * px转dip
     */
    public static int pxToDip(float pxValue) {
        final float scale = MyLib.getInstance().getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值
     */
    public static int sp2px(float spValue) {
        final float fontScale = MyLib.getInstance().getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}

package com.hjd.apputils.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by HJD on 2018/10/17 0017 and 17:23.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 * Live beautifully,dream passionately,love completely.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 * <p>
 * 自定义GridView
 */
public class CustomGridView extends GridView {


    public CustomGridView(Context context) {
        super(context);
    }

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

package com.hjd.apputils.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by HJD on 2018/8/12 0012 and 11:56.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 * Live beautifully,dream passionately,love completely.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 * <p>
 * 解决滑动冲突·
 */

public class ScrollInterceptScrollView extends ScrollView {
    private int downX, downY;
    private int mTouchSlop;

    public ScrollInterceptScrollView(Context context) {
        this(context, null);
    }

    public ScrollInterceptScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollInterceptScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollInterceptScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getRawY();
                // 判断是否滑动，若滑动就拦截事件
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
                break;
            default:
                break;
        }
        super.onInterceptTouchEvent(ev);
        return false;
    }
}


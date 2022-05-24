package com.hjd.apputils.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.hjd.apputils.R;
import com.hjd.apputils.custom.LoadingDialog;
import com.hjd.apputils.utils.AppManager;
import com.hjd.apputils.utils.CommonUtils;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public abstract class BaseActivity extends FragmentActivity {
    private TextView tvTitle;
    private boolean toastAutoCancel = true;
    private static final String TAG = "uploadImage";
    public static final Map<String, String> param = new HashMap<>();

    private LoadingDialog loadingDialog;
    /**
     * activity跳转tag
     */
    private String mActivityJumpTag;
    /**
     * activity跳转时间
     */
    private long mClickTime;

    /**
     * 当activity pause时候  toast是否自动取消
     *
     * @param toastAutoCancel
     */
    public void setToastAutoCancel(boolean toastAutoCancel) {
        this.toastAutoCancel = toastAutoCancel;
    }

    /**
     * 在oncreate后调用 修改标题
     *
     * @param title
     */
    public void setTitle(String title) {
        tvTitle.setText(title);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(returnLayoutResID());
        /*这行防止软键盘弹出时上面的空间错乱套*/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        CommonUtils.initState(this, R.color.contract_bar_col);
        loadingDialog = new LoadingDialog(this);

        AppManager.getInstance().addActivity(this);

        tvTitle = (TextView) findViewById(R.id.head_title);
        if (tvTitle != null) {
            setTitle(setTitleInitLayout());
        } else {
            setTitleInitLayout();
        }
        initView(savedInstanceState);
        initData();
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    }


    /**
     * 检查是否重复跳转，不需要则重写方法并返回true
     */
    protected boolean checkDoubleClick(Intent intent) {

        // 默认检查通过
        boolean result = true;
        // 标记对象
        String tag;
        if (intent.getComponent() != null) { // 显式跳转
            tag = intent.getComponent().getClassName();
        } else if (intent.getAction() != null) { // 隐式跳转
            tag = intent.getAction();
        } else {
            return true;
        }

        if (tag.equals(mActivityJumpTag) && mClickTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false;
        }

        // 记录启动标记和时间
        mActivityJumpTag = tag;
        mClickTime = SystemClock.uptimeMillis();
        return result;
    }

    /**
     * 为了兼容老框架的initVIew 方法
     */
    public void initView(Bundle savedInstanceState) {
    }

    public void initData() {

    }

    /**
     * return一个布局文件 用来设置当前的activity
     *
     * @return
     */
    public abstract int returnLayoutResID();

    /**
     * 设置一个标题
     *
     * @return
     */
    public abstract String setTitleInitLayout();

    /**
     * 获取标题String
     */
    public String getTitleString() {
        return tvTitle.getText().toString();
    }

    /**
     * activity销毁方法
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }


    public void setRightButton(int Resid) {
        ImageView rightButton = (ImageView) findViewById(R.id.head_right_button);
        rightButton.setImageResource(Resid);
        rightButton.setVisibility(View.VISIBLE);
    }

    public void setRightButtonText(String s) {
        TextView rightButton = (TextView) findViewById(R.id.head_right_text_button);
        rightButton.setText(s);
        rightButton.setVisibility(View.VISIBLE);
    }

    public void setRightButtonTextColor(int color) {
        TextView rightButton = (TextView) findViewById(R.id.head_right_text_button);
        rightButton.setTextColor(color);
        rightButton.setVisibility(View.VISIBLE);
    }

/*
    public void setTitleLineColor(int color) {
        View view = findViewById(R.id.view_title_line);
        view.setBackgroundColor(color);
    }*/

    public void rightClick(View v) {

    }

    public void rightIVClick(View v) {

    }

    public void rightTVClick(View v) {

    }

    /**
     * 无参数打开一个activi
     *
     * @author guoyi
     * @title 修改跳转页的标题
     */
    public static <T> void gotoActivity(Context context, Class<T> clazz, String... title) {
        Intent intent = new Intent(context, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        if (title != null) {
            intent.putExtra("titleString", title);
        }
        context.startActivity(intent);
        intent = null;
    }

    /**
     * 参数打开一个activi
     *
     * @author guoyi
     * @params 参数
     */
    public static <T> void gotoActivity(Context context, Class<T> clazz, HashMap<String, Object> params) {
        Intent intent = new Intent(context, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        Iterator iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            intent.putExtra((String) entry.getKey(), String.valueOf(entry.getValue()));
        }
        context.startActivity(intent);
    }


    public boolean isEmp(CharSequence charSequence) {
        return TextUtils.isEmpty(charSequence);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /*设置文字左面的图片*/
    public void setLeftDrawable(int resid) {
        TextView leftButton = (TextView) findViewById(R.id.head_left_text_button);
        Drawable leftDrawable = ContextCompat.getDrawable(this, resid);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        leftButton.setCompoundDrawables(leftDrawable, null, null, null);
    }

    /*设置文字上面的图片*/
    public void setTopDrawable(int resid) {
        TextView leftButton = (TextView) findViewById(R.id.head_left_text_button);
        Drawable leftDrawable = ContextCompat.getDrawable(this, resid);
        leftDrawable.setBounds(0, leftDrawable.getMinimumWidth(), 0, leftDrawable.getMinimumHeight());
        leftButton.setCompoundDrawables(leftDrawable, null, null, null);
    }

    public void setLeftDrawableAndTextColor(int resid, int color) {
        TextView leftButton = (TextView) findViewById(R.id.head_left_text_button);
        leftButton.setTextColor(color);
        leftButton.setTextColor(color);
        Drawable leftDrawable = ContextCompat.getDrawable(this, resid);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        leftButton.setCompoundDrawables(leftDrawable, null, null, null);
    }

    public String getIntentStringExtra(String key) {
        String result = this.getIntent().getStringExtra(key);
        if (result == null) {
            throw new NullPointerException("参数空指针,请检查传参");
        }
        return result;
    }

    /*   隐藏软键盘方法   -------------------------------------------------------------------------  */

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
                v.clearFocus();//点击空白处时清除焦点
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，
     * 来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            ((EditText) v).setCursorVisible(false);
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /*     ----------------------------------------------------------------------------------*/

    public LoadingDialog showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setCancelable(false);
            loadingDialog.show();
        }
        return loadingDialog;
    }

    public void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        super.onDestroy();
        AppManager.getInstance().removeActivity(this);
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }
}

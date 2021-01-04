package com.hjd.applib.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewbinding.ViewBinding;

import com.hjd.applib.R;
import com.hjd.applib.custom.LoadingDialog;
import com.hjd.applib.utils.CommonUtils;
import com.socks.library.KLog;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kr.co.namee.permissiongen.PermissionGen;


public abstract class BaseBindingActivity<T extends ViewBinding> extends FragmentActivity {

    protected T binding;

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
        Type superClass = getClass().getGenericSuperclass();
        Class<?> aClass = (Class<?>) ((ParameterizedType) superClass).getActualTypeArguments()[0];
        try {
            Method method = aClass.getDeclaredMethod("inflate", LayoutInflater.class);
            binding = (T) method.invoke(null, getLayoutInflater());
            setContentView(binding.getRoot());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        /*这行防止软键盘弹出时上面的空间错乱套*/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        CommonUtils.initState(this, R.color.contract_bar_col);
        loadingDialog = new LoadingDialog(this);
        initPhotoError();//解决7.0上相机问题

        initView();
        initData();

        /*读取存取权限， 位置信息， 手机状态，录音权限(录小视频用的)，
         * */
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (checkDoubleClick(intent)) {
            super.startActivityForResult(intent, requestCode, options);
        }
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
    public void initView() {
    }

    public void initData() {

    }

    /**
     * android 7.0系统解决拍照的问题
     */
    private void initPhotoError() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
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
            //            OkGo.getInstance().cancelTag(this);
            KLog.i("取消请求");
        }
    }

    @Subscribe
    @Override
    protected void onDestroy() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }
}

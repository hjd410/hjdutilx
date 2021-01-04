package com.hjd.applib.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewbinding.ViewBinding;

import com.google.gson.Gson;
import com.hjd.applib.R;
import com.hjd.applib.custom.LoadingDialog;
import com.hjd.applib.utils.Event;
import com.hjd.applib.utils.EventUtil;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by HJD on 2021/1/4 0004 and 11:29.
 */


public abstract class BaseBindingFragment<T extends ViewBinding> extends Fragment {
    protected T binding;
    /**
     * 贴附的activity
     */
    protected FragmentActivity mActivity;
    public static final Map<String, String> map = new HashMap<>();
    public static final String json = new Gson().toJson(map);

    /**
     * 根view
     */
    protected View mRootView;

    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;
    /**
     * 是否加载完成
     * 当执行完oncreatview,View的初始化方法后方法后即为true
     */
    protected boolean mIsPrepare;

    private boolean clickable = true;


    private LoadingDialog loadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = getActivity();
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Type superClass = getClass().getGenericSuperclass();
        Class<?> aClass = (Class<?>) ((ParameterizedType) superClass).getActualTypeArguments()[0];
        Class[] parameterTypes;
        try {
            Method method = aClass.getDeclaredMethod("inflate", LayoutInflater.class);
            binding = (T) method.invoke(null, getLayoutInflater(), container, false);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }


        if (isRegisterEventBus()) {
            EventUtil.register(this);
        }
        initPhotoError();//解决7.0相机问题
        loadingDialog = new LoadingDialog(mActivity);
        initData(getArguments());

        initView();

        mIsPrepare = true;

        onLazyLoad();

        return binding.getRoot();
    }

    public FragmentActivity getmActivity() {
        return mActivity;
    }

    private void initPhotoError() {
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
    }

    /**
     * 是否注册事件分发
     *
     * @return true绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected boolean isRegisterEventBus() {
        return EventBus.getDefault().isRegistered(this);
    }

    /**
     * 接收到分发到事件
     *
     * @param event 事件
     */
    protected void receiveEvent(Event event) {

    }

    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    protected void receiveStickyEvent(Event event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(Event event) {
        if (event != null) {
            receiveStickyEvent(event);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()) {
            EventUtil.unregister(this);
        }
    }

    /**
     * 初始化数据
     *
     * @param arguments 接收到的从其他地方传递过来的参数
     */
    protected void initData(Bundle arguments) {

    }

    /**
     * 初始化View
     */
    protected void initView() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        this.mIsVisible = isVisibleToUser;

        if (isVisibleToUser) {
            onVisibleToUser();
        }
    }

    /**
     * 用户可见时执行的操作
     */
    protected void onVisibleToUser() {
        if (mIsPrepare && mIsVisible) {
            onLazyLoad();
        }
    }

    /**
     * 懒加载，仅当用户可见切view初始化结束后才会执行
     */
    protected void onLazyLoad() {
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


    public LoadingDialog showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mActivity);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
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
    public void onDestroyView() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        //        OkGo.getInstance().cancelTag(this);
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        clickable = true;
    }

    protected boolean isClicked() {
        return clickable;
    }

    protected void lookClick() {
        clickable = false;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (isClicked()) {
            lookClick();
            super.startActivityForResult(intent, requestCode, options);
        }
    }
}

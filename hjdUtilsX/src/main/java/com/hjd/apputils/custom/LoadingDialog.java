package com.hjd.apputils.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.hjd.apputils.R;
import com.wang.avi.AVLoadingIndicatorView;


/**
 * Created by HJD on 2019/2/28/0028 and 19:27.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 * Live beautifully,dream passionately,love completely.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 * <p>
 * 加载中。。。    Dialog
 */
public class LoadingDialog extends AlertDialog {

    private Context context;
    private static LoadingDialog loadingDialog;
    private AVLoadingIndicatorView avi;

    public static LoadingDialog getInstance(Context context) {
        loadingDialog = new LoadingDialog(context, R.style.TransparentDialog); //设置AlertDialog背景透明
        int divierId = context.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = loadingDialog.findViewById(divierId);
        if (divider != null) {
            divider.setBackgroundColor(Color.TRANSPARENT);
        }
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        return loadingDialog;
    }

    public LoadingDialog(@NonNull Activity activity) {
        super(activity, R.style.TransparentDialog);
        this.context = activity;
        //设置AlertDialog背景透明
        loadingDialog = new LoadingDialog(activity, R.style.TransparentDialog);
        int divierId = activity.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = loadingDialog.findViewById(divierId);
        if (divider != null) {
            divider.setBackgroundColor(Color.TRANSPARENT);
        }
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
    }

    private LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.loading);
        avi = (AVLoadingIndicatorView) this.findViewById(R.id.loading);
    }


    @Override
    public void show() {//开启
        super.show();
    }

    @Override
    public void dismiss() {//关闭
        super.dismiss();
    }

}

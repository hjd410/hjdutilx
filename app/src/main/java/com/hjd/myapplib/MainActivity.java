package com.hjd.myapplib;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import com.hjd.applib.base.BaseBindingActivity;
import com.hjd.applib.utils.ToastUtils;
import com.hjd.myapplib.databinding.ActivityMainBinding;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {


    @Override
    public void initData() {
        setPermissions(Manifest.permission.CAMERA);
        binding.tv.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                ToastUtils.showShort("1111");

            }
        });
    }

    @Override
    public int checkPermission(String permission, int pid, int uid) {
        return super.checkPermission(permission, pid, uid);
    }
}
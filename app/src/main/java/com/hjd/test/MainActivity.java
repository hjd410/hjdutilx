package com.hjd.test;


import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.hjd.apputils.base.BaseBindingActivity;
import com.hjd.apputils.utils.StatusBarUtil;
import com.hjd.apputils.utils.ToastUtils;
import com.hjd.test.databinding.ActivityMainBinding;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;


public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {

    MusicService.SimpleBinder bindService;

    private boolean isBind;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bindService = (MusicService.SimpleBinder) service;
            bindService.doTask();
            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
            LogUtils.i("是个啥:" + name.toString());
        }
    };

    @Override
    protected void initView() {

    }

    @Override
    public void initData() {
        setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        showLoadingDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissLoading();
            }
        }, 5000);

        Glide.with(this).load("http://p1.pstatp.com/large/166200019850062839d3").into(binding.img);

        binding.tv.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
//                ToastUtils.showShort("1111");
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/appTest/星辰大海.mp3");

                Intent intent = new Intent(MainActivity.this, MusicService.class);
                intent.putExtra("path", file.getPath());
                bindService(intent, serviceConnection, BIND_AUTO_CREATE);
             /*   Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.122:8081/").build();
                TestApi api = retrofit.create(TestApi.class);
                api.getBlog().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse
                            (Call<ResponseBody> call, Response<ResponseBody> response) {

                        try {
                            LogUtils.d(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        LogUtils.e(t.getMessage());
                    }
                });*/
            }
        });

        binding.tv1.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (isBind) {
                    unbindService(serviceConnection);
                    ToastUtils.showShort("解绑服务");
                    isBind = false;
                } else {
                    ToastUtils.showShort("还没绑定服务");
                }

            }
        });
    }



    @Override
    public int checkPermission(String permission, int pid, int uid) {
        return super.checkPermission(permission, pid, uid);
    }

    @Target(TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ContentView {
        int value();
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
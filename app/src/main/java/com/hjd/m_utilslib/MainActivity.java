package com.hjd.m_utilslib;


import android.view.View;

import com.bumptech.glide.Glide;
import com.hjd.apputils.base.BaseBindingActivity;
import com.hjd.apputils.utils.StatusBarUtil;
import com.hjd.apputils.utils.ToastUtils;
import com.hjd.m_utilslib.databinding.ActivityMainBinding;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static java.lang.annotation.ElementType.*;


public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {


    @Override
    public void initData() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);

        Glide.with(this).load("http://p1.pstatp.com/large/166200019850062839d3").into(binding.img);

        binding.tv.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                ToastUtils.showShort("1111");
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.122:8081/").build();
                TestApi api = retrofit.create(TestApi.class);
                api.getBlog().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse
                            (Call<ResponseBody> call, Response<ResponseBody> response) {

                        try {
                            Logger.d(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Logger.e(t.getMessage());
                    }
                });
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
}
package com.hjd.myapplib;


import android.Manifest;
import android.view.View;

import com.hjd.applib.base.BaseBindingActivity;
import com.hjd.applib.utils.StatusBarUtil;
import com.hjd.applib.utils.ToastUtils;
import com.hjd.myapplib.databinding.ActivityMainBinding;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {


    @Override
    public void initData() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);

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
}
package com.hjd.applib.app;

import android.app.Application;
import android.content.Context;

import com.hjd.applib.network.AuthImageDownloader;
import com.hjd.applib.utils.TokenInterceptor;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import okhttp3.OkHttpClient;

/**
 * Created by HJD on 2021/1/4 0004 and 9:57.
 */
public class MyLib {

    Application context;

    private MyLib() {
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(config);
        initImageLoader();
        //OkGo网络请求初始化3.X
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        builder.addInterceptor(new TokenInterceptor());
    }

    public static MyLib getInstance() {
        return Utils.lib;
    }

    private static class Utils {
        private static MyLib lib = new MyLib();
    }

    public MyLib init(Application application) {
        context = application;
        OkGo.getInstance().init(application);
        return this;
    }

    public Context getContext() {
        return context;
    }


    /*ImageLoader图片加载*/
    private void initImageLoader() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "/backImg");
//        KLog.e( cacheDir );
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .defaultDisplayImageOptions(displayOption())
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                //替换允许Https的图片加载
                .imageDownloader(new AuthImageDownloader(context))
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    private DisplayImageOptions displayOption() {
        DisplayImageOptions options = new DisplayImageOptions
                .Builder()
                .displayer(new RoundedBitmapDisplayer(50))
                .cacheOnDisk(true)
                .build();
        return options;
    }
}

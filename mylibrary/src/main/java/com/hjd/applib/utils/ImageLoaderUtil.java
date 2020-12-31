package com.hjd.applib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hjd.applib.R;
import com.hjd.applib.app.UApplication;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.ByteArrayOutputStream;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 网络请求工具类——图片缓存用
 */
public class ImageLoaderUtil {

    private static ImageLoaderUtil instance;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mListItemOptions;

    public DisplayImageOptions circleOptions;


    private ImageLoaderUtil() {
        mImageLoader = ImageLoader.getInstance();
        mListItemOptions = new DisplayImageOptions.Builder()
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.mipmap.load_default_img)
                .showImageOnLoading(R.mipmap.load_default_img)
                // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(R.mipmap.load_default_img)
                // 加载图片时会在内存、磁盘中加载缓存
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .delayBeforeLoading(300)
                .build();
        init();
    }


    public static ImageLoaderUtil getInstance() {
        if (instance == null) {
            instance = new ImageLoaderUtil();
        }
        return instance;
    }

    private DisplayImageOptions options;
    private ImageLoader mUiImageLoader;

    // configBuilder.imageDownloader();
    private void init() {
        mUiImageLoader = ImageLoader.getInstance();
//        if (!ImageLoader.getInstance().isInited()) {
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.load_default_img)
                .showImageOnLoading(R.mipmap.load_default_img)
                .showImageOnFail(R.mipmap.load_default_img)
                // 不缓存在内存中
                .cacheInMemory(false)
                .cacheOnDisk(true)
                // 设置图片的缩放类型
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                // 设置图片的质量
                .bitmapConfig(Bitmap.Config.RGB_565)
                // 设置图片以何种编码方式显示
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();
        ImageLoaderConfiguration.Builder configBuilder = new ImageLoaderConfiguration.Builder(UApplication.getContext());
        configBuilder.defaultDisplayImageOptions(options)
                .memoryCache(new UsingFreqLimitedMemoryCache(3 * 1024 * 1024))
                .memoryCacheSize(3 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .memoryCacheExtraOptions(420, 420)
                .memoryCache(new WeakMemoryCache())
                .threadPoolSize(5);
        ImageLoaderConfiguration config = configBuilder.build();
        mUiImageLoader.init(config);

        circleOptions = new DisplayImageOptions.Builder()
//                .showStubImage(MessageLSAdapter.drawable.) // 设置图片下载期间显示的图片
//                .showImageForEmptyUri(MessageLSAdapter.drawable.demo_little_icon) // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(MessageLSAdapter.drawable.demo_little_icon) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(90)) // 设置成圆角图片
                .build();

    }

    public void load(String uri, ImageView imageAware) {
        if (TextUtils.isEmpty(uri)) {
            return;
        }
        if (imageAware == null) {
            return;
        }
        if (mUiImageLoader != null && options != null) {
            try {
                mUiImageLoader.displayImage(uri, imageAware, options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadRes(int uri, ImageView imageAware) {
        load("drawable://" + uri, imageAware);
    }

    public void loadDisk(String uri, ImageView imageAware) {
        load("file:///mnt/sdcard/" + uri, imageAware);
    }

    public void loadCri(String url, ImageView img) {
        mImageLoader.displayImage(url, img, circleOptions);
    }

    public Bitmap loadBitmapByHttp(String uri) {
        return mUiImageLoader.loadImageSync(uri);
    }


    /**
     * 加载圆形图片
     *
     * @param mContext
     * @param
     * @param target
     * @param isAvatar
     */
    public static void loadImageCircle(Context mContext, byte[] byteFile, ImageView target, boolean... isAvatar) {
        boolean avatar = isAvatar.length > 0 && isAvatar[0];

        if (avatar) {
            try {
                Glide.with(mContext)//
                        .load(R.mipmap.ic_launcher)//
                        .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))//
                        .into(target);//
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (byteFile == null) {
            return;
        }
        try {
            Glide.with(mContext)//
                    .load(byteFile)
                    .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))//
                    .into(target);//
        } catch (Exception e) {
            e.printStackTrace();
        }
        target.setVisibility(View.VISIBLE);
    }


    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 列表图片
     *
     * @param uri
     * @param imageView
     */
    public void displayListItemImage(String uri, ImageView imageView) {
        String strUri = (isEmpty(uri) ? "" : uri);
        mImageLoader.displayImage(strUri, imageView, mListItemOptions);
    }

    private boolean isEmpty(String str) {
        if (str != null && str.trim().length() > 0 && !str.equalsIgnoreCase("null")) {
            return false;
        }
        return true;
    }


}
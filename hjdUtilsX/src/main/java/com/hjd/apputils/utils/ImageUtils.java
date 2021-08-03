package com.hjd.apputils.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by HJD on 2020/2/29 and 16:44.
 * <p>
 * 将网络图片下载到本地，在获取
 */
public class ImageUtils {
    public static final String PATH = "/mypicture/con/";
    private static ImageView showImageView;
    private static String mUrl;
    private static String filePath;

    public static String getFileName(String url) {
        int index = url.lastIndexOf("/") + 1;
        return url.substring(index);
    }

    public static void setImageBitmap(String url, ImageView iv) {
        showImageView = iv;
        mUrl = url;
        Bitmap loacalBitmap = ImageUtils.getLoacalBitmap(Environment.getExternalStorageDirectory() + ImageUtils.PATH + ImageUtils.getFileName(url));
        if (loacalBitmap != null) {
            showImageView.setImageBitmap(loacalBitmap);
            Log.d("ImageUtils", "本地获取");
        } else {
            Log.d("ImageUtils", "网络获取");
            new DownImgAsyncTask().execute(url);
        }
    }

    public static String setImageFilePath(String url) {
        mUrl = url;
        Bitmap loacalBitmap = ImageUtils.getLoacalBitmap(Environment.getExternalStorageDirectory() + ImageUtils.PATH + ImageUtils.getFileName(url));
        if (loacalBitmap != null) {
            showImageView.setImageBitmap(loacalBitmap);
            Log.d("ImageUtils", "本地获取");
        } else {
            Log.d("ImageUtils", "网络获取");
        }
        new DownImgAsyncTask().execute(url);
        return filePath;
    }

    /**
     * 通过URL地址获取Bitmap对象 * @Title: getBitMapByUrl * @param @param url * @param @return * @param @throws Exception * @return Bitmap * @throws
     */
    public static Bitmap getBitmapByUrl(final String url) {
        URL fileUrl = null;
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            fileUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            Log.d("shaoace", "1");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            is = null;
        }
        return bitmap;
    }

    //保存图片到本地路径
    public static File saveImage(Bitmap bmp, String path, String fileName) {
        File appDir = new File(path);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * @Title: getLoacalBitmap * @Description: 加载本地图片 * @param @param url 本地路径 * @param @return * @return Bitmap * @throws
     */
    public static Bitmap getLoacalBitmap(String url) {
        if (url != null) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(url);
                return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fis = null;
                }
            }
        } else {
            return null;
        }
    }

    static class DownImgAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap b = getBitmapByUrl(params[0]);
            return b;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {
                File file = saveImage(result, Environment.getExternalStorageDirectory() + PATH, getFileName(mUrl));
                Log.d("ImageUtils", file.toString());
                filePath = file.getPath();
                showImageView.setImageBitmap(result);
            }
        }
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImageBitmap(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Context ac, Uri uri) {
        InputStream input = null;
        try {
            input = ac.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1)) {
            return null;
        }
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        try {
            input = ac.getContentResolver().openInputStream(uri);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        return compressImageBitmap(bitmap);//再进行质量压缩
    }
}

package com.hjd.applib.custom.clipimage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


import com.hjd.applib.R;
import com.hjd.applib.utils.ImageCompressUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ClipImageActivity extends Activity {
    private ClipImageLayout mClipImageLayout;
    private Button btnCCW;// 逆时针
    private Button btnCW;// 顺时针
    private Button btnCancel;
    private Button btnSave;

    private float degrees = 0;// 旋转角度

    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.sp_clip_main);
        mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
        btnCCW = (Button) findViewById(R.id.btn_ccw);
        btnCW = (Button) findViewById(R.id.btn_cw);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSave = (Button) findViewById(R.id.btn_save);
        initView();

    }


    public void initView() {
        initBitmap();

        btnCCW.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                degrees -= 90;
                mClipImageLayout
                        .setImageBitmap(setBitmapRotate(bitmap, degrees));
            }
        });
        btnCW.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                degrees += 90;
                mClipImageLayout
                        .setImageBitmap(setBitmapRotate(bitmap, degrees));
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bitmap bitmap = mClipImageLayout.clip();

                    // 由于Intent传递bitmap不能超过40k,此处使用二进制数组传递
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                    byte[] bitmapByte = baos.toByteArray();

                    Intent intent = new Intent();
                    intent.putExtra("bitmap", bitmapByte);
                    /*
                     * 调用setResult方法表示我将Intent对象返回给之前的那个Activity，
                     * 这样就可以在onActivityResult方法中得到Intent对象，
                     */
                    setResult(3, intent);
                    // 释放图片资源
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                    // 结束当前这个Activity对象的生命
                    finish();
                } catch (Exception e) {
                    // showToast("图片太大剪切失败，请重试");
                    e.printStackTrace();
                    finish();
                }
            }
        });
    }

    /**
     * 初始化图片资源
     */
    @SuppressWarnings("deprecation")
    private void initBitmap() {
        try {
            ImageCompressUtils compress = new ImageCompressUtils();
            ImageCompressUtils.CompressOptions options = new ImageCompressUtils.CompressOptions();
            options.uri = getIntent().getData();
            options.maxWidth = getWindowManager().getDefaultDisplay()
                    .getWidth();
            options.maxHeight = getWindowManager().getDefaultDisplay()
                    .getHeight();
            bitmap = compress.compressFromUri(this, options);

            float f = getBitmapDegree(Environment.getExternalStorageDirectory()
                    + "/temp.jpg");

            bitmap = rotateBitmapByDegree(bitmap, f);
            mClipImageLayout.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            // showToast("图片太大剪切失败，请重试");
            finish();
        }
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private float getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("旋转角度", "" + degree);
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */

    public static Bitmap rotateBitmapByDegree(Bitmap bm, float degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 图片旋转
     */
    private Bitmap setBitmapRotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, false);
    }

}

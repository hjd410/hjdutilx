package com.hjd.test;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.IOException;

/**
 * Created by HJD on 2021/7/1 and 16:05.
 */
public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private SimpleBinder simpleBinder;
    public String ss;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();

        simpleBinder = new SimpleBinder();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        ss = intent.getStringExtra("path");
        ToastUtils.showLong(ss);
        if (simpleBinder != null) {
            return simpleBinder;
        }
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("onDestroy方法");
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
        mediaPlayer.release();
    }

    class SimpleBinder extends Binder {

        public void doTask() {
            ToastUtils.showShort("dotask");
            LogUtils.i("dotask");
            try {
                mediaPlayer.setDataSource(ss);
                mediaPlayer.prepare();
                if (!mediaPlayer.isPlaying()) {
                    // 开始播放
                    mediaPlayer.start();
                    // 允许循环播放
                    mediaPlayer.setLooping(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

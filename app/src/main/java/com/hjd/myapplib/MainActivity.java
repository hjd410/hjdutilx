package com.hjd.myapplib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hjd.applib.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public int returnLayoutResID() {
        return 0;
    }

    @Override
    public String setTitleInitLayout() {
        return null;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
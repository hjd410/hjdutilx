package com.hjd.test

import android.app.Activity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ToastUtils
import com.hjd.apputils.base.BaseBindingActivity
import com.hjd.test.databinding.ActivityMainBinding

/**
 * Created by HJD on 2021/1/18 0018 and 14:50.
 */
@Route(path = "/one/showOne")
class Test : BaseBindingActivity<ActivityMainBinding>() {

    override fun initView(bundle: Bundle?) {
        TODO("Not yet implemented")
        ToastUtils.showLong("skjdfkjskdjf")
    }

    override fun initData() {
        TODO("Not yet implemented")
    }


}
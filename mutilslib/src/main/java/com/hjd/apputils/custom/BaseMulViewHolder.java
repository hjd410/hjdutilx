package com.hjd.apputils.custom;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by HJD on 2019/12/20 and 10:26.
 */

/**
 * 此处定义的意义
 * RecyclerView数据的显示放在ViewHolder中,定义Holder基类
 * 在这里多布局中，可能涉及到的多个对象， 所以基类中的对象类型使用泛型定义，
 * 必须是多布局对象基类的子类，这样在后面数据和控件绑定的时候比较方便。
 *
 * @param <T>
 */
public abstract class BaseMulViewHolder<T extends ExampleBaseBean> extends RecyclerView.ViewHolder {

    public BaseMulViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindData(T dataModel);

}

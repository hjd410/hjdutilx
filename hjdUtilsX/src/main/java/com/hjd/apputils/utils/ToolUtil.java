package com.hjd.apputils.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 大灯泡 on 2017/4/18.
 * <p>
 * 常用工具类
 */

public class ToolUtil {
    public static boolean isListEmpty (List< ? > datas) {
        return datas == null || datas.size( ) <= 0;
    }


    public static boolean checkDeviceHasNavigationBar (Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources( );
        int id = rs.getIdentifier( "config_showNavigationBar", "bool", "android" );
        if (id > 0) {
            hasNavigationBar = rs.getBoolean( id );
        }
        try {
            Class systemPropertiesClass = Class.forName( "android.os.SystemProperties" );
            Method m = systemPropertiesClass.getMethod( "get", String.class );
            String navBarOverride = (String) m.invoke( systemPropertiesClass, "qemu.hw.mainkeys" );
            if ("1".equals( navBarOverride )) {
                hasNavigationBar = false;
            } else if ("0".equals( navBarOverride )) {
                hasNavigationBar = true;
            }
        } catch ( Exception e ) {
        }
        return hasNavigationBar;
    }

    /**
     * 设定输入框输入数字范围
     *
     * @param editText
     * @param min
     * @param max
     */
    public static void setEditTextRange (final EditText editText, final int min, final int max) {
        Map< EditText, TextWatcher > mEdtWatcherMap = new HashMap<>( );
        TextWatcher watcher = mEdtWatcherMap.get( editText );
        if (null != watcher) {
            editText.removeTextChangedListener( watcher );
        }
        watcher = new TextWatcher( ) {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @SuppressLint ( "SetTextI18n" )
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (! s.toString( ).isEmpty( )) {
                    int value = Integer.valueOf( s.toString( ) );
                    String num = autoGenericCode( "000", 3 );

                    if (value < min) {

                        editText.setText( min + "" );
                    } else if (value > max) {
                        editText.setText( max + "" );
                    }
                } else {
                    editText.setText( min + "" );
                }
            }

            @Override
            public void afterTextChanged (Editable s) {

            }
        };
        editText.addTextChangedListener( watcher );

        mEdtWatcherMap.put( editText, watcher );
    }

    public String dataLong (int c)// 这个方法是保证时间两位数据显示，如果为1点时，就为01
    {
        if (c >= 100)
            return String.valueOf( c );
        else
            return "0" + String.valueOf( c );
    }

    /**
     * 不够位数的在前面补0，保留code的长度位数字
     *
     * @param code
     * @return
     */
    public static String autoGenericCode (String code) {
        String result = "";
        // 保留code的位数
        result = String.format( "%0" + code.length( ) + "d", Integer.parseInt( code ) );

        return result;
    }

    /**
     * 不够位数的在前面补0，保留num的长度位数字
     *
     * @param code
     * @return
     */
    public static String autoGenericCode (String code, int num) {
        String result = "";
        if (code.equals( "0" ) | code.equals( "00" ) | code.equals( "000" )) {
            // 保留num的位数
            // 0 代表前面补充0
            // num 代表长度为4
            // d 代表参数为正数型
            result = String.format( "%0" + num + "d", Integer.parseInt( code + 1 ) );
        } else {
            // 保留num的位数
            // 0 代表前面补充0
            // num 代表长度为4
            // d 代表参数为正数型
            result = String.format( "%0" + num + "d", Integer.parseInt( code ) );
        }
        return result;
    }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;

    public static boolean isFastClick ( ) {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis( );
        if (( curClickTime - lastClickTime ) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}

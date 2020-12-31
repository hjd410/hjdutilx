package com.hjd.applib.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class CommonUtils {
    /**
     * 无参数打开一个activi
     *
     * @author guoyi
     * @title 修改跳转页的标题
     */
    public static < T > void startActivity (Context context, Class< T > clazz, String... title) {
        Intent intent = new Intent( context, clazz );
        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS );
        if (title != null) {
            intent.putExtra( "titleString", title );
        }
        context.startActivity( intent );
        intent = null;
    }

    /**
     * 参数打开一个activi
     *
     * @author guoyi
     * @params 参数
     */
    public static < T > void startActivity (Context context, Class< T > clazz, HashMap< String, Object > params) {
        Intent intent = new Intent( context, clazz );
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS );
        Iterator iterator = params.entrySet( ).iterator( );
        while ( iterator.hasNext( ) ) {
            Map.Entry entry = (Map.Entry) iterator.next( );
            intent.putExtra( (String) entry.getKey( ), String.valueOf( entry.getValue( ) ) );
        }
        context.startActivity( intent );
    }


    public static < T > void startIntActivity (Context context, Class< T > clazz, HashMap< Integer, Integer > integer) {
        Intent intent = new Intent( context, clazz );
        Iterator iterator = integer.entrySet( ).iterator( );
        while ( iterator.hasNext( ) ) {
            Map.Entry entry = (Map.Entry) iterator.next( );
            intent.putExtra( (String) entry.getKey( ), (String) entry.getValue( ) );
        }
        context.startActivity( intent );
    }

    /*选择本地文件*/
/*
    public static void pickFile(Activity context) {
        Intent intent1 = new Intent(context, NormalFilePickActivity.class);
        intent1.putExtra(Constant.MAX_NUMBER, 3);
        intent1.putExtra(IS_NEED_FOLDER_LIST, true);
        intent1.putExtra(NormalFilePickActivity.SUFFIX,
                new String[]{"xlsx", "xls", "docx", "doc", "pptx", "ppt", "pdf"});
        context.startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_FILE);
    }

    public static void pickPhoto(Activity context) {
        Intent intent1 = new Intent(context, ImagePickActivity.class);
        intent1.putExtra(Constant.MAX_NUMBER, 9);
        intent1.putExtra(IS_NEED_FOLDER_LIST, true);
        intent1.putExtra(ImagePickActivity.IS_NEED_IMAGE_PAGER,
                new String[]{"xlsx", "xls", "docx", "doc", "pptx", "ppt", "pdf"});
        context.startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }
*/
    public static void showSoftInput (Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.toggleSoftInput( 0, InputMethodManager.HIDE_NOT_ALWAYS );
        //imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideSoftInput (Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.hideSoftInputFromWindow( view.getWindowToken( ), 0 ); //强制隐藏键盘
    }


    /**
     * 清空通知栏
     *
     * @param context
     */
    public void clearnAllNotify (Context context) {
        /** Notification管理 */
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );
        mNotificationManager.cancelAll( );
    }


    /**
     * 解决ScrollView嵌套ListView只显示一行的问题
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren (ListView listView) {
        ListAdapter listAdapter = listView.getAdapter( );
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for ( int i = 0 ; i < listAdapter.getCount( ) ; i++ ) {
            View listItem = listAdapter.getView( i, null, listView );
            listItem.measure( 0, 0 );
            totalHeight += listItem.getMeasuredHeight( );
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams( );
        params.height = totalHeight + ( listView.getDividerHeight( ) * ( listAdapter.getCount( ) - 1 ) );
        listView.setLayoutParams( params );
    }


    /**
     * 判断最后listView中最后一个item是否完全显示出来
     *
     * @return
     */
    public static boolean isLastItemVisible (ListView listView) {
        final Adapter adapter = listView.getAdapter( );

        if (null == adapter || adapter.isEmpty( )) {
            return true;
        }

        final int lastItemPosition = adapter.getCount( ) - 1;
        final int lastVisiblePosition = listView.getLastVisiblePosition( );


        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - listView.getFirstVisiblePosition( );
            final int childCount = listView.getChildCount( );
            final int index = Math.min( childIndex, childCount - 1 );
            final View lastVisibleChild = listView.getChildAt( index );
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom( ) <= listView.getBottom( );
            }
        }

        return false;
    }


    /**
     * root权限吓 静默安装
     *
     * @return
     */
    public static boolean isRoot ( ) {
        boolean result = false;
        Process process = null;
        OutputStream os = null;
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            //成功代表root
            process = Runtime.getRuntime( ).exec( "su" );
            os = process.getOutputStream( );
            os.write( "pm install -r".getBytes( ) );
            os.flush( );
            os.write( "exit\n".getBytes( ) );
            process.waitFor( );
            br = new BufferedReader( new InputStreamReader( process.getErrorStream( ) ) );
            sb = new StringBuffer( );
            String line;
            while ( ( line = br.readLine( ) ) != null ) {
                sb.append( line );
            }
            if (! sb.toString( ).contains( "Failure" )) {
                result = true;
            }
        } catch ( Exception e ) {
            e.printStackTrace( );
            result = false;
        } finally {
            try {
                if (os != null) {
                    os.close( );
                }
                if (br != null) {
                    br.close( );
                }
            } catch ( Exception e ) {
                os = null;
                br = null;
                process.destroy( );
            }

        }

        return result;
    }

    /**
     * 改变状态栏颜色
     *
     * @param window
     * @param color
     */
    public static void changeStateBarColor (Window window, int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION );
            //        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            ////                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            ////                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS );
            window.setStatusBarColor( color );   //这里动态修改颜色
        }
    }

    /**
     * 改变textview指定位置文字颜色(前景色)
     *
     * @param textView
     * @param start
     * @param end
     * @param color
     */
    public static void changeTextColor (TextView textView, int start, int end, int color) {

        SpannableStringBuilder builder = new SpannableStringBuilder( textView.getText( ).toString( ) );
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan redSpan = new ForegroundColorSpan( color );
        builder.setSpan( redSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE );
        textView.setText( builder );
    }

    /**
     * 跳转到应用市场的详情页中
     *
     * @param context   上下文
     * @param appPkg    目标APP的包名
     * @param marketPkg 应用商店的包名。如果为"" 则由系统弹出应用商店列表供用户选择，否则跳转到
     *                  目标市场的应用详情界面
     */
    public static void launchAppDetail (Context context, String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty( appPkg ))
                return;

            Uri uri = Uri.parse( "market://details?id=" + appPkg );
            Intent intentAPP = new Intent( Intent.ACTION_VIEW, uri );
            if (! TextUtils.isEmpty( marketPkg )) {
                intentAPP.setPackage( marketPkg );
            }
            intentAPP.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            context.startActivity( intentAPP );
        } catch ( Exception e ) {
            e.printStackTrace( );
        }
    }

    /**
     * 通过包名检查应用有没有安装
     *
     * @param context
     * @param packagename
     * @return
     */
    public static boolean isAppInstalled (Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager( ).getPackageInfo( packagename, 0 );
        } catch ( PackageManager.NameNotFoundException e ) {
            packageInfo = null;
            e.printStackTrace( );
        }
        if (packageInfo == null) {
            //System.out.println("没有安装");
            Uri uri = Uri.parse( "https://mobile.baidu.com/item?pid=825114773&f0=home_homeHeader%400" );
            Intent intent = new Intent( Intent.ACTION_VIEW, uri );
            context.startActivity( intent );
            return false;
        } else {
            //System.out.println("已经安装");
            launchAppDetail( context, "com.zd.manageesb", "com.baidu.appsearch" );
            return true;
        }
    }


    //获取版本号(内部识别号)
    public static int getVersionCode (Context context) {
        try {
            PackageInfo pi = context.getPackageManager( ).getPackageInfo( context.getPackageName( ), 0 );
            return pi.versionCode;
        } catch ( PackageManager.NameNotFoundException e ) {
            e.printStackTrace( );
            return 0;
        }
    }

    //    沉浸式状态栏
    public static void initState (Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow( ).addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS );
            activity.getWindow( ).setStatusBarColor( activity.getResources( ).getColor( color ) );
        }
    }


    /**
     * 测量View的宽高
     *
     * @param view View
     */
    public static void measureWidthAndHeight (View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec( 0, View.MeasureSpec.UNSPECIFIED );
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec( 0, View.MeasureSpec.UNSPECIFIED );
        view.measure( widthMeasureSpec, heightMeasureSpec );
    }





}
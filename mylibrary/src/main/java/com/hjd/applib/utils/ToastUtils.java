package com.hjd.applib.utils;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by HJD on 2018/7/5 0005 and 14:52.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 * Live beautifully,dream passionately,love completely.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 * 比较实用的Toast工具
 */

public class ToastUtils {
    private static boolean isShow = true;// 默认显示
    private static Toast mToast = null; //全局唯一

    private ToastUtils() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    public static void controlShow(boolean isShowToast) {
        isShow = isShowToast;
    }

    /**
     * 取消显示吐司
     */
    public static void cancelToast() {
        if (isShow && mToast != null) {
            mToast.cancel();
        }
    }

    /**
     * 短时间显示
     *
     * @param context 上下文
     * @param message 要显示的信息
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0以上toast直接用原生的方法即可，并不用setText防止重复的显示的问题
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            } else {
                if (mToast == null) {
                    mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                } else {
                    mToast.setText(message);
                }
                mToast.show();
            }
        }
    }

    /**
     * 短时间显示
     *
     * @param context
     * @param resId   资源ID getResources().getString(MessageLSAdapter.string.)
     */
    public static void showShort(Context context, int resId) {
        if (isShow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0以上toast直接用原生的方法即可，并不用setText防止重复的显示的问题
                Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
            } else {
                if (mToast == null) {
                    mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
                } else {
                    mToast.setText(resId);
                }
                mToast.show();
            }
        }
    }

    /**
     * 长时间显示
     *
     * @param context 上下文
     * @param message 要显示的信息
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0以上toast直接用原生的方法即可，并不用setText防止重复的显示的问题
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            } else {
                if (mToast == null) {
                    mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                } else {
                    mToast.setText(message);
                }
                mToast.show();
            }
        }
    }

    /**
     * 长时间显示
     *
     * @param context 上下文
     * @param resId   资源ID getResources().getString(MessageLSAdapter.string.)
     */
    public static void showLong(Context context, int resId) {
        if (isShow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0以上toast直接用原生的方法即可，并不用setText防止重复的显示的问题
                Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
            } else {
                if (mToast == null) {
                    mToast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
                } else {
                    mToast.setText(resId);
                }
                mToast.show();
            }
        }
    }

    /**
     * 自定义显示时间
     *
     * @param context  上下文
     * @param message  信息
     * @param duration 时间
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0以上toast直接用原生的方法即可，并不用setText防止重复的显示的问题
                Toast.makeText(context, message, duration).show();
            } else {
                if (mToast == null) {
                    mToast = Toast.makeText(context, message, duration);
                } else {
                    mToast.setText(message);
                }
                mToast.show();
            }
        }
    }

    /**
     * 自定义显示时间
     *
     * @param context  上下文
     * @param resId    信息
     * @param duration 时间
     */
    public static void show(Context context, int resId, int duration) {
        if (isShow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0以上toast直接用原生的方法即可，并不用setText防止重复的显示的问题
                Toast.makeText(context, resId, duration).show();
            } else {
                if (mToast == null) {
                    mToast = Toast.makeText(context, resId, duration);
                } else {
                    mToast.setText(resId);
                }
                mToast.show();
            }
        }
    }

    /**
     * 自定义Toast的View
     *
     * @param context  上下文
     * @param message  信息
     * @param duration 时间
     * @param view     显示自己的view
     */
    public static void customToastView(Context context, CharSequence message, int duration, View view) {
        if (isShow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0以上toast直接用原生的方法即可，并不用setText防止重复的显示的问题
                Toast.makeText(context, message, duration).show();
            } else {
                if (mToast == null) {
                    mToast = Toast.makeText(context, message, duration);
                } else {
                    mToast.setText(message);
                }
                if (view != null) {
                    mToast.setView(view);
                }
                mToast.show();
            }
        }
    }

    /**
     * 自定义Toast的位置
     *
     * @param context  上下文
     * @param message  信息
     * @param duration 时间
     * @param gravity  重心
     * @param xOffset  x偏移量
     * @param yOffset  y偏移量
     */
    public static void customToastGravity(Context context, CharSequence message, int duration,
                                          int gravity, int xOffset, int yOffset) {
        if (isShow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0以上toast直接用原生的方法即可，并不用setText防止重复的显示的问题
                Toast toast = Toast.makeText(context, message, duration);
                toast.setGravity(gravity, xOffset, yOffset);
                toast.show();
            } else {
                if (mToast == null) {
                    mToast = Toast.makeText(context, message, duration);
                } else {
                    mToast.setText(message);
                }
                mToast.setGravity(gravity, xOffset, yOffset);
                mToast.show();
            }
        }
    }

    /**
     * 自定义带图片和文字的Toast，最终的效果就是上面是图片，下面是文字
     *
     * @param context
     * @param message
     * @param iconResId 图片的资源id,如:MessageLSAdapter.drawable.icon
     * @param duration
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void showToastWithImageAndText(Context context, CharSequence message, int iconResId,
                                                 int duration, int gravity, int xOffset, int yOffset) {
        if (isShow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0以上toast直接用原生的方法即可，并不用setText防止重复的显示的问题
                Toast.makeText(context, message, duration).show();
            } else {
                if (mToast == null) {
                    mToast = Toast.makeText(context, message, duration);
                } else {
                    mToast.setText(message);
                }
                mToast.setGravity(gravity, xOffset, yOffset);
                LinearLayout toastView = (LinearLayout) mToast.getView();
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(iconResId);
                toastView.addView(imageView, 0);
                mToast.show();
            }
        }
    }

    /**
     * 自定义Toast,针对类型CharSequence
     *
     * @param context
     * @param message
     * @param duration
     * @param view
     * @param isGravity        true,表示后面的三个布局参数生效,false,表示不生效
     * @param gravity
     * @param xOffset
     * @param yOffset
     * @param isMargin         true,表示后面的两个参数生效，false,表示不生效
     * @param horizontalMargin
     * @param verticalMargin
     */
    public static void customToastAll(Context context, CharSequence message, int duration, View view, boolean isGravity, int gravity, int xOffset, int yOffset, boolean isMargin, float horizontalMargin, float verticalMargin) {
        if (isShow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0以上toast直接用原生的方法即可，并不用setText防止重复的显示的问题
                Toast.makeText(context, message, duration).show();
            } else {
                if (mToast == null) {
                    mToast = Toast.makeText(context, message, duration);
                } else {
                    mToast.setText(message);
                }
                if (view != null) {
                    mToast.setView(view);
                }
                if (isMargin) {
                    mToast.setMargin(horizontalMargin, verticalMargin);
                }
                if (isGravity) {
                    mToast.setGravity(gravity, xOffset, yOffset);
                }
                mToast.show();
            }
        }
    }

    /**
     * 自定义Toast,针对类型resId
     *
     * @param context
     * @param resId
     * @param duration
     * @param view             :应该是一个布局，布局中包含了自己设置好的内容
     * @param isGravity        true,表示后面的三个布局参数生效,false,表示不生效
     * @param gravity
     * @param xOffset
     * @param yOffset
     * @param isMargin         true,表示后面的两个参数生效，false,表示不生效
     * @param horizontalMargin
     * @param verticalMargin
     */
    public static void customToastAll(Context context, int resId, int duration, View view,
                                      boolean isGravity, int gravity, int xOffset, int yOffset,
                                      boolean isMargin, float horizontalMargin, float verticalMargin) {
        if (isShow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0以上toast直接用原生的方法即可，并不用setText防止重复的显示的问题
                Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
            } else {
                if (mToast == null) {
                    mToast = Toast.makeText(context, resId, duration);
                } else {
                    mToast.setText(resId);
                }
                if (view != null) {
                    mToast.setView(view);
                }
                if (isMargin) {
                    mToast.setMargin(horizontalMargin, verticalMargin);
                }
                if (isGravity) {
                    mToast.setGravity(gravity, xOffset, yOffset);
                }
                mToast.show();
            }
        }
    }
}

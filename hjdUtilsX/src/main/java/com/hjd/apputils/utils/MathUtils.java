package com.hjd.apputils.utils;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hjd.apputils.app.MyLib;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 各种数学工具
 *
 * @author wangpc
 */
public class MathUtils {

    /**
     * <p>
     * 方法描述: 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * </p>
     * * @param context
     * 承接上下文
     *
     * @param dpValue 将要转换的dp值
     */
    public static int dip2px(float dpValue) {
        final float scale = MyLib.getInstance().getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * <p>
     * 方法描述: 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * </p>
     * <p>
     * 承接上下文
     *
     * @param pxValue 将要转换的px值
     */
    public static int px2dip(float pxValue) {
        final float scale = MyLib.getInstance().getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * <p>
     * 方法描述: 已知两个经纬度，求余弦角（正北方向顺时针夹角）
     * </p>
     *
     * @param latitude1
     * @param longitude1
     * @param latitude2
     * @param longitude2
     */
    public static double dot2angle(double latitude1, double longitude1, double latitude2, double longitude2) {
        double a = latitude2 - latitude1;
        double b = longitude2 - longitude1;
        double course = Math.atan(b / a) / Math.PI * 180;
        if (a < 0) {
            course += 180;
        }
        return course;
    }

    /**
     * <p>
     * 方法描述:实际距离换算成字符串
     * </p>
     *
     * @param distance
     * @return
     */
    public static String getDistance(float distance) {
        float dis = distance / 1000;
        if (distance < 1000.0) {
            int d = (int) distance;
            return String.valueOf(d) + "m";
        } else if (dis < 100.0) {
            return String.valueOf(dis).substring(0, 4) + "km";
        } else {
            return (int) dis + "km";
        }

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 获取距离当前时间几天或几分钟 规则：当天显示：几分钟几小时前 1-100天：显示几天前 当年显示：几月几日 非当年显示：某年某月某日
     *
     * @param date 接收String类型的日期字符串，格式为yyyy-MM-dd HH:mm:ss
     * @return
     * @author changbl
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimesAgo(String date) {
        if (!TextUtils.isEmpty(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date now = new Date();
                // long timediff = now.getTime() - sdf.parse(date).getTime();
                long timediff = now.getTime() - Long.parseLong(date);
                long miniute = timediff / 1000 / 60;
                long hour = miniute / 60;
                long day = hour / 24;
                if (miniute >= 60) {
                    if (hour >= 24) {
                        if (day >= 1 && day < 100) {
                            return day + "天前";
                        } else {
                            if (date.substring(0, 4).equals(sdf.format(now).substring(0, 4))) {
                                return date.substring(5, 10);
                            }
                            return date.substring(0, 10);
                        }
                    }
                    return hour + "小时前";
                } else if (miniute == 0) {
                    return "刚刚";
                }
                return miniute + "分钟前";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 用时间戳算时间差 格式 1小时1分1秒
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 时间差
     */
    public static String getTimeMargin(String startTime, String endTime) {
        long startT = Long.parseLong(startTime);
        long endT = Long.parseLong(endTime);
        long marginT = endT - startT;
        long days = marginT / (1000 * 60 * 60 * 24);
        long hours = (marginT % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (marginT % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (marginT % (1000 * 60)) / 1000;
        String day = "";
        String hour = "";
        String minute = "";
        String second = "1";
        if (days > 0) {
            day = days + "天";
        }
        if (hours > 0) {
            hour = hours + "时";
        }
        if (minutes > 0) {
            minute = minutes + "分";
        }
        if (seconds > 0) {
            second = seconds + "秒";
        }
        return day + hour + minute + second;
    }

    public static String getTime24(String time) {
        Date date = new Date(Long.valueOf(time));
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }

    /**
     * 四舍五入保留两位小数
     *
     * @param aa
     * @return
     */
    public static String get4down5up2d(Double aa) {
        DecimalFormat f = new DecimalFormat("#,##0.00");
        return f.format(aa);
    }

    public static String get4down5up2d(float aa) {
        DecimalFormat f = new DecimalFormat("#,##0.00");
        return f.format(aa);
    }

    public static String get4down5up2d(String aa) {
        DecimalFormat f = new DecimalFormat("#,##0.00");

        return f.format(Double.parseDouble(aa));
    }

    /**
     * 小数点限制位数(限制成为价格标准)
     *
     * @param editText
     */
    public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });

    }

    private static String nums[] = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    private static String pos_units[] = {"", "十", "百", "千"};

    private static String weight_units[] = {"", "万", "亿"};


    /**
     * 数字转汉字【新】
     *
     * @param num
     * @return
     */
    public static String numberToChinese(int num) {
        if (num == 0) {
            return "零";
        }

        int weigth = 0;//节权位
        String chinese = "";
        String chinese_section = "";
        boolean setZero = false;//下一小节是否需要零，第一次没有上一小节所以为false
        while (num > 0) {
            int section = num % 10000;//得到最后面的小节
            if (setZero) {//判断上一小节的千位是否为零，是就设置零
                chinese = nums[0] + chinese;
            }
            chinese_section = sectionTrans(section);
            if (section != 0) {//判断是都加节权位
                chinese_section = chinese_section + weight_units[weigth];
            }
            chinese = chinese_section + chinese;
            chinese_section = "";

            setZero = (section < 1000) && (section > 0);
            num = num / 10000;
            weigth++;
        }
        if ((chinese.length() == 2 || (chinese.length() == 3)) && chinese.contains("一十")) {
            chinese = chinese.substring(1, chinese.length());
        }
        if (chinese.indexOf("一十") == 0) {
            chinese = chinese.replaceFirst("一十", "十");
        }

        return chinese;
    }

    /**
     * 将每段数字转汉子
     *
     * @param section
     * @return
     */
    public static String sectionTrans(int section) {
        StringBuilder section_chinese = new StringBuilder();
        int pos = 0;//小节内部权位的计数器
        boolean zero = true;//小节内部的置零判断，每一个小节只能有一个零。
        while (section > 0) {
            int v = section % 10;//得到最后一个数
            if (v == 0) {
                if (!zero) {
                    zero = true;//需要补零的操作，确保对连续多个零只是输出一个
                    section_chinese.insert(0, nums[0]);
                }
            } else {
                zero = false;//有非零数字就把置零打开
                section_chinese.insert(0, pos_units[pos]);
                section_chinese.insert(0, nums[v]);
            }
            pos++;
            section = section / 10;
        }

        return section_chinese.toString();
    }


}

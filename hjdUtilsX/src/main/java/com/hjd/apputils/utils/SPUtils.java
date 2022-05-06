package com.hjd.apputils.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.hjd.apputils.app.MyLib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by HJD on 2020/1/19 and 13:59.
 */
public class SPUtils {


    /**
     * 保存实体类
     *
     * @param t        要保存的实体类
     * @param fileName 文件名
     * @param keyName  键值名称
     * @param <T>
     */

    public static <T> void saveBean2Sp(T t, String fileName, String keyName) {
        SharedPreferences preferences = MyLib.getInstance().getContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        ByteArrayOutputStream bos;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(t);
            byte[] bytes = bos.toByteArray();
            String ObjStr = Base64.encodeToString(bytes, Base64.DEFAULT);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(keyName, ObjStr);
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.flush();
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * @param fileName 文件名
     * @param keyNme   键值名
     * @param <T>
     * @return
     */
    public static <T extends Object> T getBeanFromSp(String fileName, String keyNme) {
        SharedPreferences preferences = MyLib.getInstance().getContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        byte[] bytes = Base64.decode(preferences.getString(keyNme, ""), Base64.DEFAULT);
        ByteArrayInputStream bis;
        ObjectInputStream ois = null;
        T obj = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }


    /**
     * 存放实体类以及任意类型 * @param context 上下文对象 * @param key * @param obj
     */
    public static void putBean(String key, Object obj) {
        if (obj instanceof Serializable) {// obj必须实现Serializable接口，否则会出问题
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                String string64 = new String(Base64.encode(baos.toByteArray(),
                        0));
                SharedPreferences.Editor editor = MyLib.getInstance().getContext().getSharedPreferences(String.valueOf(obj), Context.MODE_PRIVATE).edit();
                editor.putString(key, string64).commit();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            throw new IllegalArgumentException(
                    "the obj must implement Serializble");
        }

    }

    public static Object getBean(String key) {
        Object obj = null;
        try {
            String base64 = MyLib.getInstance().getContext().getSharedPreferences(key, Context.MODE_PRIVATE).getString(key, "");
            if (base64.equals("")) {
                return null;
            }
            byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

}

//package com.hjd.apputils.utils;
//
//
//import java.lang.reflect.Type;
//import java.util.List;
//import java.util.Map;
//
//
//public class GsonUtils {
//    public static Gson gson;
//
//    static {
//        gson = new Gson();
//    }
//
//    public static Gson getGson() {
//        if (gson == null) {
//            gson = new Gson();
//        }
//        return gson;
//    }
//
//    /**
//     * 转化成bean
//     *
//     * @param json
//     * @param clazz
//     * @param <T>
//     * @return
//     */
//    public static <T> T fromJson(String json, Class<T> clazz) {
//        return gson.fromJson(json, clazz);
//    }
//
//    public static <T> T fromJson(String json, Type type) {
//        return gson.fromJson(json, type);
//    }
//
//    /**
//     * 转成list
//     *
//     * @param gsonString
//     * @param cls
//     * @return
//     */
//    public static <T> List<T> GsonToList(String gsonString, Class<T> cls) {
//        List<T> list = null;
//        if (gson != null) {
//            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
//            }.getType());
//        }
//        return list;
//    }
//
//    /**
//     * 转成list中有map的
//     *
//     * @param gsonString
//     * @return
//     */
//    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
//        List<Map<String, T>> list = null;
//        if (gson != null) {
//            list = gson.fromJson(gsonString,
//                    new TypeToken<List<Map<String, T>>>() {
//                    }.getType());
//        }
//        return list;
//    }
//
//    /**
//     * 转成map的
//     *
//     * @param gsonString
//     * @return
//     */
//    public static <T> Map<String, T> GsonToMaps(String gsonString) {
//        Map<String, T> map = null;
//        if (gson != null) {
//            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
//            }.getType());
//        }
//        return map;
//    }
//
//
//    public static String toJson(Object T) {
//        Gson gson = new Gson();
//        String result = gson.toJson(T);
//        return result;
//    }
//}

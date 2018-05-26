package com.mountain.jsview;

import com.google.gson.Gson;

public class JsonUtil {
    private static Gson sGson = new Gson();

    public static <T> T fromJson(String json, Class<T> tClass) {
        return sGson.fromJson(json, tClass);
    }

    public static String toJson(Object object) {
        return sGson.toJson(object);
    }
}

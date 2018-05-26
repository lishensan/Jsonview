package com.mountain.JsView.recycleview;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class DataSource {

    private HashMap<String, Object> dataSouce = new HashMap<>(8);

    public DataSource() {
        dataSouce.put("view_type", 0);
    }

    public Object putData(String key, Object value) {
        return dataSouce.put(key, value);
    }

    public Object removeData(String key) {
        return dataSouce.remove(key);
    }

    public Object getData(String key){
        return dataSouce.get(key);
    }
    public static void main(String[] args){
        DataSource dataSource = new DataSource();
        dataSource.putData("meta","nihao");
        Gson gson = new Gson();
        String s = gson.toJson(dataSource);
        System.out.println(s);
    }
}

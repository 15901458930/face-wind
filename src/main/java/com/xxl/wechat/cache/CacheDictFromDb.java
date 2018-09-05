package com.xxl.wechat.cache;

import com.jfinal.json.FastJson;

import java.util.Map;

/**
 * 从数据库缓存字典类
 */
public class CacheDictFromDb {


    public static void main(String[] args) {
       Map<String,String> map =  FastJson.getJson().parse("{\"a\":\"aaa\",\"b\":\"bbb\",\"c\":\"ccc\"}",Map.class);
        System.out.println("");
    }
}

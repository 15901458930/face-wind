package com.xxl.wechat.constant;

import java.util.HashMap;
import java.util.Map;

public class DictConstant {

    public final static Map<String,String> ASSET_TYPE = new HashMap<>();

    public final static Map<String,Map<String,String>> ASSET_SUB_TYPE = new HashMap<>();

    static{

       // ASSET_TYPE.put("10","桌椅板凳");
        ASSET_TYPE.put("20","信息分类");

        Map<String,String> map1 = new HashMap<>();
        map1.put("1001","办公室电脑");
        map1.put("1002","办公室打印机");
        map1.put("1003","笔记本");
        map1.put("1004","手机");
        map1.put("1005","班机展台");
        map1.put("1006","ipad");
        map1.put("1007","办公室IP电话");
        map1.put("1008","班级多媒体");
        ASSET_SUB_TYPE.put("20",map1);

       /* Map<String,String> map2 = new HashMap<>();
        map2.put("2001","板凳");
        map2.put("2002","桌子");
        map2.put("2003","黑板擦");
        map2.put("2004","椅子");
        map2.put("2005","窗子");*/
       // ASSET_SUB_TYPE.put("10",map2);


    }





}

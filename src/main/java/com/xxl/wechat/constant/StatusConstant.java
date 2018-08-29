package com.xxl.wechat.constant;

import java.util.HashMap;
import java.util.Map;

public class StatusConstant {

    public static Map<Integer,String> statusMap  = new HashMap<>();

    /**
     * 未处理
     */
    public final static  int WAIT_PROCESS = 1;

    /**
     *  处理中
     */
    public final static  int IN_PROCESS = 2;

    /**
     * 没有问题
     */
    public final static  int NO_PROBLEM = 3;

    /**
     * 上报领导
     */
    public final static  int NOTICE_LEADER = 4;

    /**
     * 慢慢等待
     */
    public final static  int JUST_WAIT = 5;

    /**
     * 联系厂商
     */
    public final static  int CONTACT_FACTORY = 6;

    /**
     * 处理完成
     */
    public final static  int CANCEL = 7;

    static{
        initStatusMap();
    }


    public static void initStatusMap(){

        statusMap.put(WAIT_PROCESS,"未处理");
        statusMap.put(IN_PROCESS,"处理中");
        statusMap.put(NO_PROBLEM,"没有问题");
        statusMap.put(NOTICE_LEADER,"已通知领导");
        statusMap.put(JUST_WAIT,"慢慢等待");
        statusMap.put(CONTACT_FACTORY,"联系厂家");
        statusMap.put(CANCEL,"处理完成");
    }

    public static String getStatusMap(int key){

        return statusMap.get(key);
    }
}

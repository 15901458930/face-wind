package com.xxl.wechat.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {


    public final static String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public final static String NO_SECONDS_PATTERN = "yyyy-MM-dd HH:mm";


    public final static String MM_DD_HH_PATTERN = "MM-dd HH";


    public final static String HH_MM_PATTERN = "HH:mm";


    public final static String YMDH_PATTERN = "yyyyMMddHH";


    public final static String YMD_PATTERN = "yyyy-MM-dd";

    public final static String YM_PATTERN = "yyyyMM";

    public final static String Y_PATTERN = "yyyy";

    public static String format(Date date, String pattern) {


        if(date == null){
           return "";
        }

        return DateFormatUtils.format(date, pattern);
    }

    public static String getCurrentDateStr() {
        return format(new Date(), DEFAULT_PATTERN);
    }

    public static String getYMDHStr() {
        return format(new Date(), YMDH_PATTERN);
    }

    public static String getYMDStr() {
        return format(new Date(), YMD_PATTERN);
    }

    public static String getYMStr() {
        return format(new Date(), YM_PATTERN);
    }

    public static String getYStr() {
        return format(new Date(), Y_PATTERN);
    }


    public static Date getCurrentDate() {
        return new Date();
    }

    public static Date parseDate(String date,String pettern){
        SimpleDateFormat sdf = new SimpleDateFormat(pettern);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("字符串："+date+"转化Date失败!!!");
        }
    }

    //由出生日期获得年龄
    public static int getAgeByBirth(Date birthDay) {
        Calendar cal = Calendar.getInstance();

        if (birthDay == null) {
            return 0;
        }

        if (cal.before(birthDay)) {
            return 0;
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        return age;
    }

    public static String getPreDayZeroStr(){
       Date date =  getPreDay();
       return format(date,YMD_PATTERN)+" 00:00:00";
    }

    public static Date getPreDay() {
       return getAnotherDay(new Date(),-1);
    }

    public static Date getAnotherDay(Date date,int gap) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, gap);
        date = calendar.getTime();
        return date;
    }


    public static String getCurTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }


    public static void main(String[] args) {
        System.out.println(DateUtil.parseDate("2018-07-23 12:22",DateUtil.NO_SECONDS_PATTERN));
    }

}

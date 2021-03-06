package com.neuedu.utils;

import com.sun.org.apache.regexp.internal.RE;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * date转化工具类
 */
public class DateUtils {

    private static  final String STANDARD_FORNAT="yyyy-MM-dd HH:mm:ss";
    /**
     * date--string
     */
    //自定义格式
    public static String dateToString(Date date, String formate){
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formate);
    }
    //默认格式
    public static String dateToString(Date date){
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORNAT);
    }

    /**
     * string --date
     */
    //自定义格式
    public static Date strToDate(String str,String formate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formate);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }
    //默认格式
    public static Date strToDate(String str){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORNAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }

    public static void main(String[] args) {
        System.out.println(dateToString(new Date()));
        System.out.println(strToDate("2019-01-08 19:38:25"));
    }
}

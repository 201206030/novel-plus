package com.java2nb.novel.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 日期工具
 * @author cd
 */
public class DateUtil {

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_PATTERN = "HH:mm:ss";

    /**
     * 获取昨天的日期时间
     * */
    public static Date getYesterday(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 根据日期，获取当天开始时间
     * */
    public static Date getDateStartTime(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        /*
        * Calendar.HOUR_OF_DAY:是指获取24小时制的小时,取值范围:0-23;
        * Calendar.HOUR:是指获取12小时制的小时,取值范围:0-12,凌晨和中午都是0,不是12;
        * 需要配合Calendar.AM_PM使用;
        * */
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    /**
     * 根据日期，获取当天结束时间
     * */
    public static Date getDateEndTime(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        return calendar.getTime();
    }

    /**
     * 获取上个月开始时间
     *
     * @return
     */
    public static Date getLastMonthStartTime(){
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, -1);
        // 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获取上个月结束时间
     *
     * @return
     */
    public static Date getLastMonthEndTime(){
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, -1);
        // 获取当前月最后一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }


    /**
     * 格式化日期
     * */
    public static String formatDate(Date date,String patten){

        return new SimpleDateFormat(patten).format(date);
    }


    /**
     * 将日期格式化成"多久之前"的格式
     * */
    public static String formatTimeAgo(Date date){
        if (date == null) {
            return null;
        }

        long now = new Date().getTime();
        long then = date.getTime();

        long diff = now - then;

        if (diff < 0) {
            // 未来时间
            DateUtil.formatDate(date, DateUtil.DATE_TIME_PATTERN);
        }

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long months = days / 30;
        long years = months / 12;

        if (seconds < 60) {
            return "刚刚";
        } else if (minutes < 60) {
            return minutes + "分钟前";
        } else if (hours < 24) {
            return hours + "小时前";
        } else if (days < 30) {
            return days + "天前";
        } else if (months < 12) {
            return months + "个月前";
        } else {
            return years + "年前";
        }
    }


    public static void main(String[] args) {
        System.out.println(formatDate(getYesterday(),DATE_TIME_PATTERN));
        System.out.println(formatDate(getDateStartTime(getYesterday()),DATE_TIME_PATTERN));
        System.out.println(formatDate(getDateEndTime(getYesterday()),DATE_TIME_PATTERN));
        System.out.println(formatDate(getLastMonthStartTime(),DATE_TIME_PATTERN));
        System.out.println(formatDate(getLastMonthEndTime(),DATE_TIME_PATTERN));
    }



}

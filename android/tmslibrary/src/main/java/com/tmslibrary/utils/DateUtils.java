package com.tmslibrary.utils;

import android.content.Context;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.tmslibrary.R;
/**
 * Created by cty on 2016/12/22.
 */

public class DateUtils {


    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    public static String getTimeByFormat(Date date,String f) {
        SimpleDateFormat format = new SimpleDateFormat(f);
        return format.format(date);
    }




    public static String getYDM(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(date);
    }

    public static String getTimeFormat(Date date,String f) {
        SimpleDateFormat format = new SimpleDateFormat(f);
        return format.format(date);
    }


    public static String getTimeHHmmss(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date);
    }

    public static Date getDate(String str){


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        if(!TextUtils.isEmpty(str) && str.length() >= 19){
            str = str.substring(0,19);
            try {
                date = format.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return date;
    }
    public static Date getDateByFormat(String str,String f){

        SimpleDateFormat format = new SimpleDateFormat(f);
        Date date = new Date();

        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return date;
    }
    public static String getTimeByString(String str,String strformat){

        SimpleDateFormat format = new SimpleDateFormat(strformat);
        Date date = getDate(str);

        return format.format(date);
    }


    public static long getDelay(String str,String format,long delayTime){
        Date date = getDateByFormat(str,format);

        long delay = date.getTime()-delayTime-System.currentTimeMillis();
        return delay;

    }

    public static String transFormData(String str,String format1,String format2){
        Date date = getDateByFormat(str,format1);
        SimpleDateFormat sdf = new SimpleDateFormat(format2);
        return sdf.format(date);

    }
    public static String getDayOfMonth(long ts){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ts);
        return  calendar.get(Calendar.DAY_OF_MONTH)+"";
    }
    public static String getMonthYear(long ts){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ts);
        String month = "";
        int year = calendar.get(Calendar.YEAR);
        switch (calendar.get(Calendar.MONTH)){
            case 0:
                month = "January";
                break;
            case 1:
                month = "February";
                break;
            case 2:
                month = "March";
                break;
            case 3:
                month = "April";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "June";
                break;
            case 6:
                month = "July";
                break;
            case 7:
                month = "August";
                break;
            case 8:
                month = "September";
                break;
            case 9:
                month = "October";
                break;
            case 10:
                month = "November";
                break;
            case 11:
                month = "December";
                break;

        }

        return month+" , "+year;

    }

    public static boolean isSameDay(Date date1, Date date2) {
        String s_d1 = getYDM(date1);
        String s_d2 = getYDM(date2);
        return s_d1.equals(s_d2);
    }

    public static long getTodayStartTime(long ts) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(ts));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    public static long getTodayEndTime(long ts) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(ts));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime().getTime();
    }




    public static int getYear(long ts){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(ts));
        return  cal.get(Calendar.YEAR);
    }

    public static int getDayOfYear(long ts){
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(1);
        cal.setTime(new Date(ts));
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    public static int getWeekOfYear(long ts){
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(1);
        cal.setTime(new Date(ts));
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public static String getDayStatus(Context context,long ts){
        Date date = new Date(ts);
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String str = df.format(date);
        int a = Integer.parseInt(str);
        if (a >= 0 && a <= 12) {
            return context.getString(R.string.moring);
        }
        if (a > 12 && a <= 18) {
            return context.getString(R.string.afternoom);
        }
        return context.getString(R.string.night);
    }


    public static String getVideoTime(String s){

        if(TextUtils.isEmpty(s)){
            return "00:00";
        }

        int second = 0;
        try {
            second = (int)Float.parseFloat(s);
        }catch (Exception e){
        }

        if(second < 60){
            return  "00:"+(second<10?"0"+second:second);
        }else if(second < 3600){
            int min = second/60;
            int sec = second%60;
            return (min<10?"0"+min:min)+":"+(sec<10?"0"+sec:sec);
        }else{
            int hour = second/3600;
            int min = second%3600/60;
            int sec = second%3600%60;
            return hour + ":"+(min<10?"0"+min:min)+":"+(sec<10?"0"+sec:sec);
        }
    }


    public static String getVideoTimeMark(String s){

        if(TextUtils.isEmpty(s)){
            return "00:00";
        }

        int second = 0;
        try {
            second = (int)Float.parseFloat(s);
        }catch (Exception e){
        }

        if(second < 60){
            return  "00:"+(second<10?"0"+second:second);
        }else if(second < 3600){
            int min = second/60;
            int sec = second%60;
            return (min<10?"0"+min:min)+":"+(sec<10?"0"+sec:sec);
        }else{
            int hour = second/3600;
            int min = second%3600/60;
            int sec = second%3600%60;
            return (hour<10?"0"+hour:hour) + ":"+(min<10?"0"+min:min)+":"+(sec<10?"0"+sec:sec);
        }
    }

    public static String getQaTime(long ts,Context context){
        long currentDate = new Date().getTime();
        long offset = currentDate - ts;
        if(offset < 60 * 1000){
            //小于一分钟显示刚刚
            return context.getString(R.string.right_now);
        }else if(offset < 60*60*1000){
            //大于等于1分钟 小于1小时 显示 xx分钟前
            int minute = (int)offset/1000/60;
            return String.format(context.getString(R.string.min_before),minute);
        }else if(offset < 24 * 60 * 60 *1000){
            //大于等于1小时 小于 24小时 显示 xx小时前
            int hour = (int)offset/1000/60/60;
            return String.format(context.getString(R.string.hour_before),hour);
        }else if(offset < 48 * 60 * 60 *1000){
            //大于等于24小时 小于48小时 显示1天前
            return String.format(context.getString(R.string.day_before),1);
        }else if(offset < 72 * 60 * 60 *1000){
            //大于等于48小时 小于72小时 显示2天前
            return String.format(context.getString(R.string.day_before),2);
        }else if(offset < 96 * 60 * 60 *1000){
            //大于等于72小时 小于96小时 显示3天前
            return String.format(context.getString(R.string.day_before),3);
        }else{
            return getTimeFormat(new Date(ts),"yyyy年MM月dd日");
        }

    }

}

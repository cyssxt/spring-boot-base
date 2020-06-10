package com.cyssxt.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private final static String yyyyMMDDHHMMSSSSS ="yyyyMMddHHmmssSSS";
    private final static String yyyyMMdd="yyyyMMdd";
    private final static String HHmm="HH:mm";
    private final static String HHMN="HHmm";
    public static Timestamp getCurrentTimestamp(){
        return new Timestamp(new Date().getTime());
    }

    public static String getTimeStr() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(yyyyMMDDHHMMSSSSS);
        return simpleDateFormat.format(date);
    }
    public static String getTimeStr(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(HHmm);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static Timestamp getDateTimeWithOutTime(Timestamp time) {
        if(time==null){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getTime());
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static void main(String[] args) {
        System.out.println(getDayInteger(new Date()));
    }

    public static Integer getDayInteger(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(yyyyMMdd);
        String result = simpleDateFormat.format(date);
        return Integer.valueOf(result);
    }
    public static Integer getDayInteger() {
        return getDayInteger(new Date());
    }

    public static Integer getTimeInteger(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(HHMN);
        String result = simpleDateFormat.format(date);
        return Integer.valueOf(result);
    }

    public static Integer getTimeInteger(Integer hour,Integer minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        return getTimeInteger(calendar.getTime());
    }

    public static Date getTime(Integer hour, Integer minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }
}

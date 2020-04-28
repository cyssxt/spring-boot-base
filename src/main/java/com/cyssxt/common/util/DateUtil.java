package com.cyssxt.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private final static String YYYYMMDDHHMMSSSSS="yyyyMMddHHmmssSSS";
    public static Timestamp getCurrentTimestamp(){
        return new Timestamp(new Date().getTime());
    }

    public static String getTimeStr() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYYMMDDHHMMSSSSS);
        return simpleDateFormat.format(date);
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
        System.out.println(DateUtil.getDateTimeWithOutTime(new Timestamp(new Date().getTime())));
    }
}

package com.cyssxt.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
}

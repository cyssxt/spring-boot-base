package com.cyssxt.common.util;

import java.sql.Timestamp;
import java.util.Date;

public class DateUtil {
    public static Timestamp getCurrentTimestamp(){
        return new Timestamp(new Date().getTime());
    }
}

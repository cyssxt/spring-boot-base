package com.cyssxt.common.util;

import java.math.BigDecimal;

public class NumberUtil {

    public static boolean biggerThan(BigDecimal o1,BigDecimal o2){
        if(o1==null || o2==null){
            return false;
        }
        return o1.compareTo(o2)==1;
    }

    public static boolean biggerThanAndEqual(BigDecimal o1,BigDecimal o2){
        if(o1==null || o2==null){
            return false;
        }
        int value = o1.compareTo(o2);
        return value==1 || value==0 ;
    }
}

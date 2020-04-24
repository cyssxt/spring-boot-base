package com.cyssxt.common.util;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

public class CommonUtil {

    public static String uuid(){
        return UUID.randomUUID().toString();
    }

    private final static char[] RANDOM_CODES = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k','l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private final static int RANDOM_CODES_UPPER_LENGTH = 36;
    private final static int RANDOM_CODES_LENGTH = RANDOM_CODES.length;

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static String random(int length){
        return random(length,true);
    }

    public static String random(int length,boolean upper){
        StringBuffer buffer = new StringBuffer();
        int total = upper?RANDOM_CODES_LENGTH:RANDOM_CODES_UPPER_LENGTH;
        for(int i=0;i<length;i++){
            Random random = new Random();
            int index = random.nextInt(total);
            buffer.append(RANDOM_CODES[index]);
        }
        return buffer.toString();
    }
    /**
     * 判断对象为空
     *
     * @param obj
     *            对象名
     * @return 是否为空
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj)
    {
        if (obj == null)
        {
            return true;
        }
        if ((obj instanceof List))
        {
            return ((List) obj).size() == 0;
        }
        if ((obj instanceof String))
        {
            return ((String) obj).trim().equals("");
        }
        return false;
    }

    /**
     * 判断对象不为空
     *
     * @param obj
     *            对象名
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Object obj)
    {
        return !isEmpty(obj);
    }

    public static String generatorKey(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static String like(String value){
        return String.format("%%%s%%",value);
    }

    public static boolean isTrue(Boolean obj){
        return obj!=null && obj;
    }

    public static void main(String[] args) {
        System.out.println(CommonUtil.random(32));
    }

}

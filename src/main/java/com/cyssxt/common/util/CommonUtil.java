package com.cyssxt.common.util;

import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.response.CoreErrorMessage;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class CommonUtil {

    public static Integer getAgeByCertId(String certId) {
        String birthday = "";
        if (!StringUtils.isEmpty(certId) && certId.length() == 18) {
            birthday = certId.substring(6, 10) + "/"
                    + certId.substring(10, 12) + "/"
                    + certId.substring(12, 14);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date now = new Date();
        Date birth = new Date();
        try {
            birth = sdf.parse(birthday);
        } catch (ParseException e) {
        }
        long intervalMilli = now.getTime() - birth.getTime();
        int age = (int) (intervalMilli/(24 * 60 * 60 * 1000))/365;
//        System.out.println(age);
        return age;
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    private final static char[] RANDOM_CODES = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private final static char[] INT_CODES = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private final static int RANDOM_CODES_UPPER_LENGTH = 36;
    private final static int RANDOM_CODES_LENGTH = RANDOM_CODES.length;
    private final static int INT_CODES_LENGTH = INT_CODES.length;

    public static String stringToMD5(String plainText) throws ValidException {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new ValidException(CoreErrorMessage.MD5_NOT_EXIST);
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static String random(int length) {
        return random(length, true);
    }

    public static Integer randomInt() {
        Random random = new Random();
        return random.nextInt(999);
    }

    public static String random(int length, boolean upper) {
        StringBuffer buffer = new StringBuffer();
        int total = upper ? RANDOM_CODES_LENGTH : RANDOM_CODES_UPPER_LENGTH;
        for (int i = 0; i < length; i++) {
            Random random = new Random();
            int index = random.nextInt(total);
            buffer.append(RANDOM_CODES[index]);
        }
        return buffer.toString();
    }

    public static String randomInt(int length) {
        StringBuffer buffer = new StringBuffer();
        int total = INT_CODES_LENGTH;
        for (int i = 0; i < length; i++) {
            Random random = new Random();
            int index = random.nextInt(total);
            buffer.append(INT_CODES[index]);
        }
        return buffer.toString();
    }

    /**
     * 判断对象为空
     *
     * @param obj 对象名
     * @return 是否为空
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if ((obj instanceof List)) {
            return ((List) obj).size() == 0;
        }
        if ((obj instanceof String)) {
            return ((String) obj).trim().equals("");
        }
        return false;
    }

    /**
     * 判断对象不为空
     *
     * @param obj 对象名
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static String generatorKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String like(String value) {
        return String.format("%%%s%%", value);
    }

    public static String rightLike(String value) {
        return String.format("%s%%", value);
    }

    public static boolean isTrue(Boolean obj) {
        return obj != null && obj;
    }

    public static void main(String[] args) {
        System.out.println(getAgeByCertId("123"));
    }

    public static <T> T get(Map<String, T> carDtoMap, String carId) {
        if (carDtoMap == null) {
            return null;
        }
        return carDtoMap.get(carId);
    }


    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

}

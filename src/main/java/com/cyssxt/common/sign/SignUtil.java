package com.cyssxt.common.sign;

import com.alibaba.fastjson.JSON;
import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.response.CoreErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.text.CollationKey;
import java.text.Collator;
import java.util.*;

@Slf4j
public class SignUtil {

//    @Value("${sean.aes.key:1234567891234567}")
//    String aesKey;
//
//    @Value("${sean.signkey:1234567891234567}")
//    String signKey;

    private static char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    /**
     * md5加密
     * @param str 需要加密的字符串
     * **/
    public final static String md5(String str)
    {

        try
        {
            byte[] btInput = str.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char ch[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                ch[k++] = hexDigits[byte0 >>> 4 & 0xf];
                ch[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(ch);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


//    public String encrypt(String src) throws ValidException {
//        return encrypt(src,aesKey);
//    }
//
//    public String decrypt(String src) throws ValidException {
//        return decrypt(src,aesKey);
//    }

    public static String encrypt(String sSrc, String sKey) throws ValidException {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        try {
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

            return new Base64().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
        }catch (Exception e){
            throw new ValidException(CoreErrorMessage.ENCRYPT_ERROR);
        }
    }
    //    public static String getSign(Map<String,Object> params) throws ValidException {
//        return getSign("sign",params);
//    }
//    public static String getSign(String signName,Map<String,Object> params) throws ValidException {
//        return getSign(signKey,signName,params);
//    }
    public static String getSign(String secret, Map<String,Object> params) throws ValidException {
        return getSign(secret,"sign",params);
    }

    public static String getSign(String secret, String signName, Map<String,Object> paramMap) throws ValidException {
        paramMap = treeMapKeyLowerCase(paramMap);
        Map<String,Object> sortMap = sortMapByKey(paramMap);
        StringBuilder reqStr = new StringBuilder();
        for(Map.Entry<String,Object> entry:sortMap.entrySet()){
            String key = entry.getKey();
            if(signName.equals(key)){
                continue;
            }
            reqStr.append(key).append("=").append(entry.getValue()).append("&");
        }
        reqStr.deleteCharAt(reqStr.length()-1);
        String signData = secret+reqStr.toString();
        log.info("info={}",signData);
        String mySign = md5(signData);
        mySign = md5(mySign+secret);
        return mySign;
    }

    public static Map<String,Object> sortMapByKey(Map<String,Object> map){
        if(map==null || map.isEmpty()){
            return null;
        }
        Map<String,Object> sortMap = new TreeMap<String,Object>((o1, o2) -> {
            Collator collator = Collator.getInstance();
            CollationKey key1 = collator.getCollationKey(o1);
            CollationKey key2 = collator.getCollationKey(o2);
            return key1.compareTo(key2);
        });
        sortMap.putAll(map);
        return sortMap;
    }

    public static Map<String,Object> treeMapKeyLowerCase(Map<String,Object> map){
        String key="";
        Object value = null;
        Map<String,Object> outMap = new TreeMap<>();
        List<Map<String,Object>> list = null;
        List<Map<String,Object>> listV = new ArrayList<>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            list = new ArrayList<>();
            key = entry.getKey();
            value = entry.getValue();
            if(!isEmpty(value) && value instanceof List){
                listV = (List<Map<String, Object>>) value;
                if(listV.get(0) instanceof Map){
                    for(Map<String,Object> vMap:listV){
                        list.add(treeMapKeyLowerCase(vMap));
                    }
                    value = list;
                }
            }
            if(!isEmpty(value) && value instanceof Map){
                value = treeMapKeyLowerCase((Map<String, Object>) value);
            }
            outMap.put(key.toLowerCase(),value);
        }
        return outMap;
    }

    public static boolean isEmpty(Object str){
        return str!=null && "".equals("");
    }
    // 解密
    public static String decrypt(String sSrc, String sKey) throws ValidException {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new Base64().decode(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            throw new ValidException(CoreErrorMessage.DECRYPT_ERROR);
        }
    }

    public static void main(String[] args) throws ValidException {

        long time = new Date().getTime();
        System.out.println(time);
        String json ="{\n" +
                "  \"serviceCode\":\"5c40J2hNji12\",\n" +
                "  \"params\":{\"page\":{\n" +
                "   \n" +
                "  }},\n" +
                "  \"timestamp\":"+time+",\n" +
//                "  \"sign\":\"79ef81b0f04055101e3101aab806f8cc\"\n" +
                "}";
        Map object = JSON.parseObject(json,Map.class);
        System.out.println(JSON.toJSONString(json));
//        System.out.println();
        System.out.println(SignUtil.getSign("AO3fKuJpDgTsrplX",object));

//        System.out.println(Common);
        System.out.println(Math.ceil(1.0));
    }
}

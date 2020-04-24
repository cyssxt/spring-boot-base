package com.cyssxt.common.util;

import com.sean.datacenter.exception.ValidException;
import com.sean.datacenter.response.CoreErrorMessage;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

@Slf4j
public class PinyinUtil {
    public static String getAbbr(String str) throws ValidException {
        return getAbbr(str,true);
    }
    public static String getAbbr(String str,boolean retain) {
        HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
        hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        hanyuPinyinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        int length = str.length();
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<length;i++) {
            char ch = str.charAt(i);
            try {
                String[] temp = PinyinHelper.toHanyuPinyinStringArray(ch,hanyuPinyinOutputFormat);
                Character result = retain?ch:null;
                if(temp!=null && temp.length>0) {
                    result = temp[0].charAt(0);
                }
                buffer.append(result);
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
                log.error("parseError={},error={}",ch,badHanyuPinyinOutputFormatCombination);
            }
        }
        return buffer.toString();
    }

    public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {
        try {
            System.out.println(getAbbr("中aaa国1人1"));
        }catch (Exception | ValidException e){
            e.printStackTrace();
        }
    }
}

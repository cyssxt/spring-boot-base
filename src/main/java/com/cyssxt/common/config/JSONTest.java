package com.cyssxt.common.config;

import java.util.Arrays;
import java.util.List;

public enum  JSONTest {
    WAIT((byte)0,"aaa"),
    START((byte)1,"bbb"),
    SUCCESS((byte)2,"ccc"),
    CANCEL((byte)3,"ddd"),
    ;
    byte value;
    String msg;

    JSONTest(byte value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public static JSONTest get(Byte type){
        if(type==null){
            type = 0;
        }
        for(JSONTest travelStatusConstant:JSONTest.values()){
            if(type.byteValue()==travelStatusConstant.getValue()){
                return travelStatusConstant;
            }
        }
        return null;
    }

    public static List<Byte> getInUseStatus() {
        return Arrays.asList(new Byte[]{SUCCESS.getValue(),CANCEL.getValue()});
    }

    public byte getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }
}

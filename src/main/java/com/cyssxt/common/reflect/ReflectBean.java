package com.cyssxt.common.reflect;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class ReflectBean {
    String fieldName;
    Method method;
    Class fieldType;
    boolean isKey;
    public ReflectBean(String fieldName, Method method, Class fieldType,boolean isKey) {
        this.fieldName = fieldName;
        this.method = method;
        this.fieldType = fieldType;
        this.isKey = isKey;
    }
}

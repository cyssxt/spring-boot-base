package com.cyssxt.common.filter;

import lombok.Data;

@Data
public class KeyFilterBean {
    String key;
    KeyFilter filter;
    private KeyFilterBean(){}

    public static  KeyFilterBean build(String name){
      return build(name,null);
    }

    public static KeyFilterBean build(String name,KeyFilter keyFilter){
        KeyFilterBean keyFilterBean =  new KeyFilterBean();
        keyFilterBean.key = name;
        keyFilterBean.filter = keyFilter;
        return keyFilterBean;
    }
}

package com.cyssxt.common.filter;

import lombok.Data;

import java.util.function.Function;

@Data
public class KeyFilterView {
    String key;
    KeyFilter filter;
    private KeyFilterView(){}

    public static KeyFilterView build(String name){
      return build(name,null);
    }

    public static KeyFilterView build(Function function){
        String name = "";
        return build(name,null);
    }


    public static KeyFilterView build(String name, KeyFilter keyFilter){
        KeyFilterView keyFilterView =  new KeyFilterView();
        keyFilterView.key = name;
        keyFilterView.filter = keyFilter;
        return keyFilterView;
    }
}

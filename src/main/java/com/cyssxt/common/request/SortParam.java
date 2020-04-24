package com.cyssxt.common.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortParam {
    private String fieldName;
    private Boolean type;
    public static SortParam build(String fieldName){
        return new SortParam(fieldName,true);
    }
    public static SortParam build(String fieldName,Boolean type){
        return new SortParam(fieldName,type);
    }
}

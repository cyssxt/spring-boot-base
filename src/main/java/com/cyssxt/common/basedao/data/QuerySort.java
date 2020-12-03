package com.cyssxt.common.basedao.data;

import lombok.Data;

@Data
public class QuerySort {
    private String fieldName;
    private String type;

    private QuerySort(String fieldName, String type) {
        this.fieldName = fieldName;
        this.type = type;
    }

    public static QuerySort build(String fieldName, String type){
        return new QuerySort(fieldName,type);
    }

    public static QuerySort desc(String fieldName){
        return build(fieldName,"desc");
    }

    public static QuerySort asc(String fieldName){
        return build(fieldName,"asc");
    }

    public String value() {
        return fieldName + " " +type;
    }
}

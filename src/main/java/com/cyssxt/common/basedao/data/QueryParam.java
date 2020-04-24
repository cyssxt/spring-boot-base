package com.cyssxt.common.basedao.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryParam {
    String key;
    Object value;
    QueryExpression expression = QueryExpression.EQUAL;

    public QueryParam(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public static QueryParam Builder(String key,String value){
        return new QueryParam(key,value);
    }
}

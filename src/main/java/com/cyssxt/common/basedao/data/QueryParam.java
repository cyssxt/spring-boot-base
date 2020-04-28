package com.cyssxt.common.basedao.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

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

    public static QueryParam notDel(){
        return new QueryParam("del_flag",false);
    }

    public static QueryParam in(List<String> rowIds){
        return new QueryParam("row_id",rowIds, QueryExpression.IN);
    }
    public static QueryParam in(String key,Set<String> rowIds){
        return new QueryParam(key,rowIds, QueryExpression.IN);
    }
    public static QueryParam in(Set<String> rowIds){
        return new QueryParam("row_id",rowIds, QueryExpression.IN);
    }
}

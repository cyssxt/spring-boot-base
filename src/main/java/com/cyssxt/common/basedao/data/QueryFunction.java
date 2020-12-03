package com.cyssxt.common.basedao.data;

import com.cyssxt.common.util.CommonUtil;
import lombok.Data;

import java.util.function.Function;

@Data
public class QueryFunction<T,V>{
    String key = null;
    Function<T,V> function;
    String value;
    Expression expression = Expression.EQUAL;
    Param param;

    public QueryFunction(String key, Function function) {
        this.key = key;
        this.function = function;
    }

    public static QueryFunction equal(String key, Function function) {
        return new QueryFunction(key,function);
    }
    public QueryFunction(String value) {
        this.value = value;
    }

    public QueryFunction(String key, Function function, String value,Param param) {
        this.key = key;
        this.function = function;
        this.value = value;
        this.param = param;
    }

    public QueryFunction(String key, Function function, String value) {
        this.key = key;
        this.function = function;
        this.value = value;
    }

    public enum  Expression {
        IN,EQUAL
    }
    public enum  Param {
        LIKE,ORIGIN
    }
}

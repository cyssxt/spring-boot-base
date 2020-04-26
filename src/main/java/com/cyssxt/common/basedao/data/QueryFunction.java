package com.cyssxt.common.basedao.data;

import com.cyssxt.common.util.CommonUtil;
import lombok.Data;

import java.util.function.Function;

@Data
public class QueryFunction<T,V>{
    String key;
    Function<T,V> function;
    String value;
    Expression expression = Expression.EQUAL;
    Param param;

    public QueryFunction(String key, Function function) {
        this.key = key;
        this.function = function;
    }

    public QueryFunction(String key, Function function, String value,Param param) {
        this.key = key;
        this.function = function;
        this.value = value;
        this.param = param;
    }

    public enum  Expression {
        IN,EQUAL
    }
    public enum  Param {
        LIKE,ORIGIN
    }
}

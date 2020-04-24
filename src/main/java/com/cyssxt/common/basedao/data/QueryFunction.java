package com.cyssxt.common.basedao.data;

import lombok.Data;

import java.util.function.Function;

@Data
public class QueryFunction<T,V>{
    String key;
    Function<T,V> function;

    public QueryFunction(String key, Function function) {
        this.key = key;
        this.function = function;
    }
}

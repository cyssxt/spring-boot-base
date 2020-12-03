package com.cyssxt.common.basedao.transformer;
public interface Filter<T> {

    void callback(T t);
}

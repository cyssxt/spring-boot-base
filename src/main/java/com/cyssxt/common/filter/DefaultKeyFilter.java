package com.cyssxt.common.filter;

public class DefaultKeyFilter<T> implements KeyFilter<T> {
    @Override
    public boolean filter(T t) {
        return true;
    }
}

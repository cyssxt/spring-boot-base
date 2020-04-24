package com.cyssxt.common.model;

import lombok.Data;

import java.util.List;

@Data
public class ListResult<T> extends KeyResult {
    private List<T> data;

    public ListResult(List<T> data,List<String> keys){
        this.data = data;
        this.setKeys(keys);
    }
}

package com.cyssxt.common.model;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ListResult<T> extends KeyResult {
    private List<T> data;

    public ListResult(List<T> data, Set<String> keys){
        this.data = data;
        this.setKeys(keys);
    }
}

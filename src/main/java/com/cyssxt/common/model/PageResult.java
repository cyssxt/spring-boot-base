package com.cyssxt.common.model;

import com.cyssxt.common.response.PageResponse;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class PageResult<T> extends KeyResult {
    PageResponse<T> data;
    Map<String,Set<String>> keyMap;
    public PageResult(PageResponse<T> data, Set<String> keys){
        this.data = data;
        this.setKeys(keys);
    }

    public PageResult(PageResponse<T> data,Set<String> keys, Map<String,Set<String>> keyMap) {
        this.data = data;
        this.setKeys(keys);
        this.keyMap = keyMap;
    }

    public Set<String> getKeySet(String keyName){
        return keyMap!=null?keyMap.get(keyName):null;
    }
}

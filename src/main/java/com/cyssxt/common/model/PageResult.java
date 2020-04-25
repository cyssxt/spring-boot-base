package com.cyssxt.common.model;

import com.cyssxt.common.response.PageResponse;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PageResult<T> extends KeyResult {
    PageResponse<T> data;
    Map<String,List<String>> keyMap;
    public PageResult(PageResponse<T> data, List<String> keys){
        this.data = data;
        this.setKeys(keys);
    }

    public PageResult(PageResponse<T> data,List<String> keys, Map<String,List<String>> keyMap) {
        this.data = data;
        this.setKeys(keys);
        this.keyMap = keyMap;
    }
}

package com.cyssxt.common.model;

import com.cyssxt.common.response.PageResponse;
import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> extends KeyResult {
    PageResponse<T> data;

    public PageResult(PageResponse<T> data, List<String> keys){
        this.data = data;
        this.setKeys(keys);
    }
}

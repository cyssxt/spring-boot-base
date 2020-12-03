package com.cyssxt.common.model;

import com.cyssxt.common.basedao.transformer.KeyTransformer;
import com.cyssxt.common.response.PageResponse;
import lombok.Data;

import java.util.Set;

@Data
public class PageResult<T> extends KeyTransformerResult<PageResponse<T>> {

    public PageResult(PageResponse<T> data, Set<String> keys) {
        super(data, keys);
    }

    public PageResult(PageResponse<T> data, KeyTransformer transformer) {
        super(data, transformer);
    }
}

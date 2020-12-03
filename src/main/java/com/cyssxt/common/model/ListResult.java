package com.cyssxt.common.model;

import com.cyssxt.common.basedao.transformer.KeyTransformer;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class ListResult<T> extends KeyTransformerResult<List<T>> {
//    public ListResult(List<T> data, Set<String> keys) {
//        super(data, keys);
//    }

    public ListResult(List<T> data, KeyTransformer transformer) {
        super(data, transformer);
    }
}

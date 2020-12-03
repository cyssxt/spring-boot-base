package com.cyssxt.common.model;

import com.cyssxt.common.basedao.transformer.KeyTransformer;
import lombok.Data;
import java.util.Map;
import java.util.Set;

@Data
public class KeyTransformerResult<T> extends KeyResult{
    T data;
    Map<String, Set<String>> keyMap;
    public KeyTransformerResult(T data, Set<String> keys){
        this.data = data;
        this.setKeys(keys);
    }

    public KeyTransformerResult(T data, KeyTransformer transformer) {
        this.data = data;
        this.keys = transformer.getKeys();
        this.keyMap = transformer.getKeysMap();
    }
    public Set<String> getKeySet(String keyName){
        return keyMap!=null?keyMap.get(keyName):null;
    }
}

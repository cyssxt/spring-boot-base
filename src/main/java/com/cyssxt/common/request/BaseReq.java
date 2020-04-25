package com.cyssxt.common.request;

import com.alibaba.fastjson.TypeReference;
import com.cyssxt.common.reflect.Copy;
import lombok.Data;

@Data
public class BaseReq<T> extends Copy {
    public TypeReference<T> getTypeReference(){
        return new TypeReference<T>(){};
    };
    String sessionId;
}


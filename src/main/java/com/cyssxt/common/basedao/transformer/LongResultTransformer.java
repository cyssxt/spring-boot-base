package com.cyssxt.common.basedao.transformer;


import org.hibernate.transform.ResultTransformer;

import java.util.List;

public class LongResultTransformer implements ResultTransformer {
    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        if(tuple!=null && tuple.length>0){
            String value = tuple[0]+"";
            return Long.valueOf(value);
        }
        return null;
    }

    @Override
    public List transformList(List collection) {
        return collection;
    }
}

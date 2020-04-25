package com.cyssxt.common.basedao.transformer;

import org.hibernate.transform.TupleSubsetResultTransformer;

import java.util.List;
import java.util.Map;

public interface KeyTransformer extends TupleSubsetResultTransformer {

    List<String> getKeys();
    Map<String,List<String>> getKeysMap();
}

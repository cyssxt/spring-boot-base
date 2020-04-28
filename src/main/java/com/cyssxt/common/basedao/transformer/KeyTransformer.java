package com.cyssxt.common.basedao.transformer;

import org.hibernate.transform.TupleSubsetResultTransformer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface KeyTransformer extends TupleSubsetResultTransformer {

    Set<String> getKeys();
    Map<String, Set<String>> getKeysMap();
}

package com.cyssxt.common.basedao.transformer;

import com.cyssxt.common.filter.KeyFilter;
import com.cyssxt.common.filter.KeyFilterView;
import com.cyssxt.common.reflect.ReflectBean;
import com.cyssxt.common.reflect.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ObjectResultTransformer implements KeyTransformer {

    private static final long serialVersionUID = -3779317531110592988L;
    private final static Logger logger = LoggerFactory.getLogger(ObjectResultTransformer.class);

    private final Class<?> resultClass;
    private Set<String> keys = new HashSet<>();
    private Filter filter;
    private String keyName;
    private List<KeyFilterView> keyNames;
    private Map<String, KeyFilterView> keyFilterBeanMap;

    private Map<String, Set<String>> keyMap = new HashMap<>();
    public ObjectResultTransformer(final Class<?> resultClass,String keyName) {
        this.resultClass = resultClass;
        if(keyName==null ||keyName.equals("")){
            keyName = "row_id";
        }
        this.keyName = keyName;
    }

    public ObjectResultTransformer(final Class<?> resultClass,List<KeyFilterView> keyNames) {
        this.resultClass = resultClass;
        this.keyNames = keyNames;
        this.keyFilterBeanMap = keyNames.stream().collect(Collectors.toMap(KeyFilterView::getKey, t->t));
    }
    public ObjectResultTransformer(final Class<?> resultClass, Filter filter) {
        this.resultClass = resultClass;
        this.filter = filter;
    }
    public ObjectResultTransformer(final Class<?> resultClass) {
        this.resultClass = resultClass;
    }

    /**
     * aliases为每条记录的数据库字段名,ORACLE字段名默认为大写
     * tupe为与aliases对应的字段的值
     */
    public Object transformTuple(final Object[] tuple, final String[] aliases) {
        Object result = null;
        try {
            result = this.resultClass.newInstance();
            Map<String, ReflectBean> reflectBeans = ReflectUtils.getBeanMap(resultClass, ReflectUtils.WRITE,true);
            Map<String,Object> temp = new HashMap<>();
            for (int i=0;i<aliases.length;i++) {
                Object object = tuple[i];
                String alias = aliases[i];
                alias = alias.replace("_","").toLowerCase();
                ReflectBean reflectBean = reflectBeans.get(alias);
                if(reflectBean==null){
                    logger.debug("reflectBean is null={}",alias);
                    continue;
                }
                if(alias.equals(this.keyName) || reflectBean.isKey()){
                    keys.add(object+"");//主键转为string
                }
                if(!CollectionUtils.isEmpty(keyNames)){
                    temp.put(alias,object);
                }
                if(object!=null) {
                    ReflectUtils.copyValue(reflectBean, object, result);
                }
            }
            if(!CollectionUtils.isEmpty(temp)){
                Iterator<Map.Entry<String,Object>> iterator = temp.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<String,Object> entry = iterator.next();
                    String alias = entry.getKey();
                    Object object = entry.getValue();
                    if(!keyFilterBeanMap.containsKey(alias)){
                        continue;
                    }
                    KeyFilterView keyFilterView = keyFilterBeanMap.get(alias);
                    if(keyFilterView ==null){
                        continue;
                    }
                    KeyFilter keyFilter = keyFilterView.getFilter();
                    if(keyFilter!=null && keyFilter.filter(result)){
                        continue;
                    }
                    Set<String> items = keyMap.get(alias);
                    if(items==null){
                        items = new HashSet<>();
                        keyMap.put(alias,items);
                    }
                    items.add(object+"");
                }
            }
            if(filter!=null){
                filter.callback(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    public List transformList(final List collection) {
        return collection;
    }

    public static void main(String[] args) {
//        System.out.println(（）));
    }

    @Override
    public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
        return false;
    }

    @Override
    public boolean[] includeInTransform(String[] aliases, int tupleLength) {
        if ( aliases == null ) {
            throw new IllegalArgumentException( "aliases cannot be null" );
        }
        if ( aliases.length != tupleLength ) {
            throw new IllegalArgumentException(
                    "aliases and tupleLength must have the same length; " +
                            "aliases.length=" + aliases.length + "tupleLength=" + tupleLength
            );
        }
        boolean[] includeInTransform = new boolean[tupleLength];
        for ( int i = 0 ; i < aliases.length ; i++ ) {
            if ( aliases[ i ] != null ) {
                includeInTransform[ i ] = true;
            }
        }
        return includeInTransform;
    }

    @Override
    public Set<String> getKeys() {
        return keys;
    }

    @Override
    public Map<String, Set<String>> getKeysMap() {
        return keyMap;
    }

    public KeyFilterView getFilter(String key){
        for(KeyFilterView keyFilterView :keyNames){
            if(key.equals(keyFilterView.getKey())){
                return keyFilterView;
            }
        }
        return null;
    }
}

package com.cyssxt.common.basedao.util;

import com.cyssxt.common.basedao.transformer.KeyTransformer;
import com.cyssxt.common.basedao.transformer.LongResultTransformer;
import com.cyssxt.common.basedao.transformer.ObjectResultTransformer;
import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.model.ListResult;
import com.cyssxt.common.model.PageResult;
import com.cyssxt.common.request.PageReq;
import com.cyssxt.common.response.PageResponse;
import org.hibernate.query.internal.NativeQueryImpl;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class QueryUtil {

    private final static int ALL = -1;
    private final static int FIRST = 0;

    public static interface Parameter{
        void init(Query query);
    }

    public static <T> ListResult<T> selectList(EntityManager entityManager, String sql, Parameter parameter, KeyTransformer keyTransformer) throws ValidException {
        return selectList(entityManager,sql,parameter,keyTransformer,FIRST,ALL);
    }

    public static <T> ListResult<T> selectList(EntityManager entityManager, String sql, Parameter parameter, KeyTransformer keyTransformer,int offset,int length) throws ValidException {
        Query query = entityManager.createNativeQuery(sql);
        if(parameter!=null){
            parameter.init(query);
        }
        query.unwrap(NativeQueryImpl.class).setResultTransformer(keyTransformer);
        if(ALL!=length) {
            query.setFirstResult(offset);
            query.setMaxResults(length);
        }
        return new ListResult<T>((List<T>)query.getResultList(),keyTransformer);
    }

    public static <T> List<T> selectList(EntityManager entityManager,String sql,Parameter parameter,Class clazz) throws ValidException {
        return (List<T>) selectList(entityManager,sql,parameter,new ObjectResultTransformer(clazz)).getData();
    }

    public static  <T> PageResult<T> selectPage(EntityManager entityManager, String sql, Parameter parameter, PageReq pageReq, KeyTransformer transformer) throws ValidException {
        Query query = entityManager.createNativeQuery(sql);
        Query countQuery = entityManager.createNativeQuery(String.format("select count(*) totalnum from (%s) A",sql));
        if(parameter!=null){
            parameter.init(query);
            parameter.init(countQuery);
        }
        query.unwrap(NativeQueryImpl.class).setResultTransformer(transformer);
        countQuery.unwrap(NativeQueryImpl.class).setResultTransformer(new LongResultTransformer());
        Long total = (Long)countQuery.getSingleResult();
        int pageNo = pageReq.getPageNo();
        int pageSize = pageReq.getPageSize();
        query.setFirstResult((pageNo-1)*pageSize);
        query.setMaxResults(pageSize);
        List<T> list = query.getResultList();
        return new PageResult<T>(new PageResponse<T>(list,pageReq.getPageNo(),pageReq.getPageSize(),total),transformer);
    }

    public static  <T> PageResponse<T> selectPage(EntityManager entityManager, String sql, Parameter parameter, PageReq pageReq, Class clazz) throws ValidException {
        return (PageResponse<T>) selectPage(entityManager,sql,parameter,pageReq,new ObjectResultTransformer(clazz)).getData();
    }

    public static <T> T selectOne(EntityManager entityManager, String sql, Parameter parameter, KeyTransformer keyTransformer) throws ValidException {
        ListResult<T> result = selectList(entityManager,sql,parameter,keyTransformer,0,1);
        List data = result.getData();
        return CollectionUtils.isEmpty(data)?null:result.getData().get(0);
    }

    public static Long count(String sql,EntityManager manager,Parameter parameter){
        Query countQuery = manager.createNativeQuery(sql);
        if(parameter!=null){
            parameter.init(countQuery);
        }
        countQuery.unwrap(NativeQueryImpl.class).setResultTransformer(new LongResultTransformer());
        Long total = (Long)countQuery.getSingleResult();
        return total;
    }
}

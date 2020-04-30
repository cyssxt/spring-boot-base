package com.cyssxt.common.basedao.util;

import com.cyssxt.common.basedao.Callback;
import com.cyssxt.common.basedao.data.QueryExpression;
import com.cyssxt.common.basedao.data.QueryParam;
import com.cyssxt.common.basedao.data.QuerySort;
import com.cyssxt.common.basedao.transformer.ObjectResultTransformer;
import com.cyssxt.common.basedao.view.QueryPage;
import com.cyssxt.common.dto.BaseDto;
import com.cyssxt.common.entity.BaseEntity;
import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.model.ListResult;
import com.cyssxt.common.model.PageResult;
import com.cyssxt.common.request.PageReq;
import com.cyssxt.common.response.CoreErrorMessage;
import com.cyssxt.common.response.PageResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class QueryFactory {

    public static final String ROW_ID = "row_id";
    public static final String DEL_FLAG = "del_flag";
    @PersistenceContext
    EntityManager entityManager;

    public <T> ListResult<T> selectListByKeys(String sql, QueryUtil.Parameter parameter, Class clazz, String keyName) throws ValidException {
        return QueryUtil.selectList(entityManager,sql,parameter,new ObjectResultTransformer(clazz,keyName));
    }
    public <T> ListResult<T> selectListByKeys(String sql, QueryUtil.Parameter parameter, Class clazz) throws ValidException {
        return QueryUtil.selectList(entityManager,sql,parameter,new ObjectResultTransformer(clazz));
    }

    public <T> List<T> selectListAndData(String sql, QueryUtil.Parameter parameter, Class clazz) throws ValidException {
        return (List<T>) selectListByKeys(sql,parameter,clazz).getData();
    }

    public <T> PageResponse<T> selectPageResponse(String sql, QueryUtil.Parameter parameter, PageReq pageQuery, Class clazz) throws ValidException {
        return QueryUtil.selectPage(entityManager,sql,parameter,pageQuery,clazz);
    }

    public <T> ListResult<T> selectListAndKeys(String sql,QueryUtil.Parameter parameter,Class clazz) throws ValidException {
        return QueryUtil.selectList(entityManager,sql,parameter,new ObjectResultTransformer(clazz));
    }
    public <T> PageResult<T> selectPage(Class<? extends BaseEntity> entityClass, QueryUtil.Parameter parameter, PageReq pageQuery, Class clazz) throws ValidException {
        Table entity = entityClass.getAnnotation(Table.class);
        if(entity==null){
            throw new ValidException(CoreErrorMessage.QUERY_ENTITY_MAX_HAS_ENTITY_ANNOTATION);
        }
        String tableName = entity.name();
        String sql = String.format("select * from %s ",tableName);
        return selectPageAndKeys(sql,parameter,pageQuery,clazz,(String)null);
    }
    public <T> PageResult<T> selectPage(String sql, QueryUtil.Parameter parameter, PageReq pageQuery, Class clazz) throws ValidException {
        return selectPageAndKeys(sql,parameter,pageQuery,clazz,(String)null);
    }
    public <T> PageResult<T> selectPageAndKeys(String sql, QueryUtil.Parameter parameter, PageReq pageQuery, Class clazz,String keyName) throws ValidException{
        return QueryUtil.selectPage(entityManager,sql,parameter,pageQuery,new ObjectResultTransformer(clazz,keyName));
    }
    public <T> PageResult<T> selectPageAndKeys(String sql, QueryUtil.Parameter parameter, PageReq pageQuery, Class clazz,List<String> keyNames) throws ValidException{
        return QueryUtil.selectPage(entityManager,sql,parameter,pageQuery,new ObjectResultTransformer(clazz,keyNames));
    }
    public <T> ListResult<T> selectListByKeys(Class<? extends BaseEntity> entityClass, QueryParam[] queryParams, Class clazz) throws ValidException {
        return selectListByKeys(entityClass,queryParams,clazz,null);
    }
    public <T> ListResult<T> selectListByKeys(Class<? extends BaseEntity> entityClass, QueryParam[] queryParams, Class clazz, String keyName) throws ValidException {
        return selectListByKeys(entityClass,queryParams,clazz,null,keyName);
    }
    public <T> ListResult<T> selectListSort(Class<? extends BaseEntity> entityClass, QueryParam[] queryParams, Class clazz, QuerySort[] sorts) throws ValidException {
        return selectListByKeys(entityClass,queryParams,clazz,sorts,null);
    }
//    public <T> ListResult<T> selectList(Class<? extends BaseEntity> entityClass, QueryParam[] queryParams, Class clazz, QuerySort[] sorts) throws ValidException {
//        return selectList(entityClass,queryParams,clazz,sorts,null);
//    }
    public <T> ListResult<T> selectListByKeys(Class<? extends BaseEntity> entityClass, QueryParam[] queryParams, Class clazz, QuerySort[] sorts, String keyName) throws ValidException {
        return selectListByKeys(entityClass,queryParams,clazz,sorts,null,keyName);
    }
    public <T> ListResult<T> selectListByKeys(Class<? extends BaseEntity> entityClass, QueryParam[] queryParams, Class clazz, QuerySort[] sorts, QueryPage page, String keyName) throws ValidException {
        List<String> keyNames = new ArrayList<>();
        keyNames.add(keyName);
        return selectListByKeys(entityClass,queryParams,clazz,sorts,page,keyNames);
    }
    public <T> ListResult<T> selectListByKeys(Class<? extends BaseEntity> entityClass, QueryParam[] queryParams, Class clazz, QuerySort[] sorts, QueryPage page, List<String> keyNames) throws ValidException {
        Table entity = entityClass.getAnnotation(Table.class);
        if(entity==null){
            throw new ValidException(CoreErrorMessage.QUERY_ENTITY_MAX_HAS_ENTITY_ANNOTATION);
        }
        String tableName = entity.name();
        String sql = String.format("select * from %s ",tableName);
        List<String> params = new ArrayList<>();
        if(queryParams!=null && queryParams.length>0) {
            sql+=" where ";
            for (QueryParam queryParam : queryParams) {
                String key = queryParam.getKey();
                params.add(String.format(" %s %s :%s ", key,queryParam.getExpression().value(), key));
            }
            sql += String.join(" and ",params);
        }
        if(sorts!=null && sorts.length>0){
            sql += " order by ";
            List<String> sortValues = new ArrayList<>();
            for (QuerySort sort:sorts){
                sortValues.add(sort.value());
            }
            sql += String.join(",",sortValues);
        }
        if(page!=null){
            sql += " limit ";
            sql += page.toLimit();
        }
       return selectListByKeys(sql,queryParams,clazz,keyNames);
    }
    public <T> ListResult<T> selectListByKeys(String sql, QueryParam[] queryParams, Class clazz, List<String> keys) throws ValidException {
        QueryUtil.Parameter parameter = query -> {
            for(QueryParam queryParam:queryParams){
                Object value = queryParam.getValue();
                String key = queryParam.getKey();
                query.setParameter(key,value);
            }
        };
        return QueryUtil.selectList(entityManager,sql,parameter,new ObjectResultTransformer(clazz,keys));
    }
    public <T> ListResult<T> selectListByKeys(String sql, QueryParam[] queryParams, Class clazz, String keyName) throws ValidException {
        List<String> keys = new ArrayList<>();
        keys.add(keyName);
       return selectListByKeys(sql,queryParams,clazz,keys);
    }
    public <T> ListResult<T> selectListByKeys(String sql, QueryParam[] queryParams, Class clazz) throws ValidException {
        return selectListByKeys(sql,queryParams,clazz,(String)null);
    }
    public <T> List<T> selectListData(Class<? extends BaseEntity> entityClass, QueryParam[] queryParams, Class clazz,String keyName) throws ValidException {
       return (List<T>) selectListByKeys(entityClass,queryParams,clazz,keyName).getData();
    }
    public <T> List<T> selectListData(Class<? extends BaseEntity> entityClass, QueryParam[] queryParams, Class clazz) throws ValidException {
        return (List<T>) selectListByKeys(entityClass,queryParams,clazz).getData();
    }
    public <T> T selectOneById(Class<? extends BaseEntity> entityClass, String rowId,Class clazz) throws ValidException {
        if(StringUtils.isEmpty(rowId)){
            return null;
        }
        return selectOne(entityClass,new QueryParam[]{
                new QueryParam(ROW_ID,rowId),
                new QueryParam(DEL_FLAG,false)
        },clazz);
    }

    public <T> T selectOne(Class<? extends BaseEntity> selectClass,QueryParam[] queryParams,Class clazz) throws ValidException {
        ListResult<T> result = selectListByKeys(selectClass,queryParams,clazz);
        List<T> datas = result.getData();
        return CollectionUtils.isEmpty(datas)?null:result.getData().get(0);
    }

    public <T> T selectOne(String sql,QueryParam[] queryParams,Class clazz) throws ValidException {
        ListResult result = selectListByKeys(sql,queryParams,clazz);
        List<T> data = (List<T>) result.getData();
        if(CollectionUtils.isEmpty(data)){
            return null;
        }
        return data.get(0);
    }
    public <T  extends BaseDto> Map<String,T> selectByInRowIds(Class<? extends BaseEntity> clazz, Class<T> dtoClass, Set<String> ids) throws ValidException {
        return selectByInRowIds(clazz,dtoClass,ids,null);
    }
    public <T  extends BaseDto> Map<String,T> selectByInRowIds(Class<? extends BaseEntity> clazz, Class<T> dtoClass, Set<String> ids, Callback<T> call) throws ValidException {
        if(dtoClass==null){
            throw new ValidException(CoreErrorMessage.DTO_CLASS_NOT_EXIST);
        }
        if(CollectionUtils.isEmpty(ids)){
            return null;
        }
        ListResult<T> studentDtoListResult = selectListByKeys(clazz,
                new QueryParam[]{
                        QueryParam.notDel(),
                        QueryParam.in(ids)
                },dtoClass);
        List<T> data = studentDtoListResult.getData();
        return data.stream().collect(Collectors.toMap(BaseDto::getRowId, t -> {
            if(call!=null) {
                call.each(t);
            }
            return t;
        }));
    }
    public  <T  extends BaseDto> Map<String,T> selectByInRowIds(String sql, Class<T> dtoClass,QueryParam[] queryParams) throws ValidException {
        return selectByInRowIds(sql,dtoClass,queryParams,null);
    }
    public  <T  extends BaseDto> Map<String,T> selectByInRowIds(String sql, Class<T> dtoClass,QueryParam[] queryParams,Callback<T> call) throws ValidException {
        ListResult<T> listResult =  selectListByKeys(sql,queryParams,dtoClass);
        return listResult.getData().stream().collect(Collectors.toMap(BaseDto::getRowId, t -> {
            if(call!=null) {
                call.each(t);
            }
            return t;
        }));
    }

    public  <T  extends BaseDto> Map<String,List<T>> selectGroupByInRowIds(String sql, Class<T> dtoClass, QueryParam[] queryParams) throws ValidException {
        ListResult<T> listResult =  selectListByKeys(sql,queryParams,dtoClass);
        return listResult.getData().stream().collect(Collectors.groupingBy(BaseDto::getRowId));
    }

    public  <T  extends BaseDto> Map<String,List<T>> selectGroupByInRowIds(String sql, Class<T> dtoClass, QueryParam[] queryParams, Function<T,String> function) throws ValidException {
        ListResult<T> listResult =  selectListByKeys(sql,queryParams,dtoClass);
        return listResult.getData().stream().collect(Collectors.groupingBy(function));
    }

    public static void main(String[] args) throws ValidException {
        ListResult<BaseDto> result = new QueryFactory().selectListSort(BaseEntity.class,new QueryParam[]{
                new QueryParam("row_id",1, QueryExpression.IN)
        }, BaseDto.class,new QuerySort[]{QuerySort.asc("row_id")});
    }



}

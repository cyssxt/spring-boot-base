package com.cyssxt.common.api.service;

import com.cyssxt.common.basedao.dao.BaseRepository;
import com.cyssxt.common.basedao.data.QueryFunction;
import com.cyssxt.common.basedao.data.QuerySort;
import com.cyssxt.common.basedao.util.JpaUtil;
import com.cyssxt.common.basedao.util.QueryFactory;
import com.cyssxt.common.basedao.util.QueryUtil;
import com.cyssxt.common.dto.BaseDto;
import com.cyssxt.common.entity.BaseEntity;
import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.model.ListResult;
import com.cyssxt.common.model.PageResult;
import com.cyssxt.common.reflect.ReflectUtils;
import com.cyssxt.common.request.CreateReq;
import com.cyssxt.common.request.DelReq;
import com.cyssxt.common.request.PageReq;
import com.cyssxt.common.request.SortParam;
import com.cyssxt.common.response.CoreErrorMessage;
import com.cyssxt.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 *
 * @param <T> 数据库实体类
 * @param <V> 请求参数
 * @param <Q> 视图
 * @param <W> 分页请求参数
 */
@Slf4j
public abstract class BaseService<T extends BaseEntity, V extends CreateReq, Q extends BaseDto, W extends PageReq> {


    public abstract BaseRepository getRepository();

    public Q info(String contentId) throws ValidException {
        T t = JpaUtil.get(contentId,getRepository(),true);
        try {
            Q q = (Q) getDto().newInstance();
            beforeInfo(q,t);
            t.parse(q);
            afterInfo(q,t);
            return q;
        } catch (Exception e){
            log.error("getDto error={}",e);
            throw new ValidException(CoreErrorMessage.API_GET_DTO_ERROR);
        }
    }

    public ResponseData infoResult(String contentId) throws ValidException {
       return ResponseData.success(info(contentId));
    }

    protected void beforeInfo(Q q,T t){}

    protected void afterInfo(Q q, T t) {}

    public interface ParamConstructor{
        void addParam(List<String> params);
        void setParameters(Query query);
    }

    public T newEntity() throws ValidException {
        try {
            return (T) getEntityClass().newInstance();
        } catch (Exception e) {
            throw new ValidException(CoreErrorMessage.NEW_INSTANCE_ERROR,e);
        }
    }

    public void afterCreate(T t, V v) {
    }
    protected void beforeCreate(T t, V v) throws ValidException {}
    public void onDel(DelReq req) {
    }

    public void beforeSave(T t, V v) throws ValidException {
        log.debug("beforeSave entity={},createReq={}",t,v);
    }
    public void afterSave(T t, V v) {
        log.debug("onSave entity={},createReq={}",t,v);
    }

    public void beforeUpdate(T t, T old, V v) throws ValidException {
        log.debug("onUpdate entity={},old={},createReq={}",t,old,v);
    }

    protected String getTableName(){
        Class clazz = getEntityClass();
        Objects.requireNonNull(clazz);
        Table table = (Table) clazz.getDeclaredAnnotation(Table.class);
        return table.name();
    }

    public abstract Class getEntityClass();

    protected String getQuerySql(W w){
        return "select * from "+ getTableName() +" A ";
    }
    public void addParams(List<String> params){
    }

    public void addParams(List<String> params,W w){
    }

    public void addNotEmptyParams(List<String> params,String key,Function function,W w){
        Object value = function.apply(w);
        if (value!=null) {
            params.add(String.format(" %s=:%s ",key,key));
        }
    }
    public void addNotEmptyParams(List<String> params, QueryFunction[] queryFunctions, W w){
        for (QueryFunction queryFunction:queryFunctions) {
            addNotEmptyParams(params, queryFunction.getKey(), queryFunction.getFunction(),w);
        }
    }

    public QueryFunction[] getQueryFunctions(){
        return new QueryFunction[]{};
    }

    public String getFullQuerySql(W w){
        String querySql = getQuerySql(w);
        List<String> params = getQueryParams(w);
        QuerySort[] querySorts = getSorts(w);
        return toSql(querySql,params,querySorts);
    }

    public String toSql(String querySql,List<String> params,QuerySort[] querySorts){
        String paramQuery = String.join(" and ", params);
        String sql = querySql+" where "+ paramQuery;
        if(querySorts!=null && querySorts.length!=0) {
            List<String> sortParams = new ArrayList<>();
            for (int i=0;i<querySorts.length;i++){
                QuerySort querySort = querySorts[i];
                if(querySort!=null) {
                    sortParams.add(String.format("%s %s", querySort.getFieldName(), querySort.getType()));
                }
            }
            sql += " order by "+String.join(",",sortParams);
        }
        log.debug("query url={}",sql);
        return sql;
    }

    public String getFullQuerySql(ParamConstructor paramConstructor,W w){
        String querySql = getQuerySql(w);
        List<String> params = getQueryParams(w);
        paramConstructor.addParam(params);
        QuerySort[] querySorts = getSorts(w);
        return toSql(querySql,params,querySorts);
    }

    private List<String> initParams(){
        List<String> params = new ArrayList<>();
        params.add(" A.del_flag=0 ");
        addParams(params);
        return params;
    }

    private List<String> getQueryParams(W w){
        List<String> params = initParams();
        QueryFunction[] queryFunctions = getQueryFunctions();
        addNotEmptyParams(params,queryFunctions,w);
        return params;
    }


    protected abstract void onListQuery(W w, String sql);

    protected abstract void onPageQuery(W w, String sql);

    public abstract Class getDto();

    public QueryUtil.Parameter getQueryParameter(W w){
        return query -> setNotEmptyValues(query,w);
    }

    protected void setNotEmptyValues(Query query,W w){
        QueryFunction[] queryFunctions = getQueryFunctions();
        for(QueryFunction function:queryFunctions) {
            setNotEmptyValue(query, function.getKey(), function.getFunction(), w);
        }
    }

    protected void setNotEmptyValue(Query query,String key,Function function,W w) {
        Object value = function.apply(w);
        if(value!=null){
            query.setParameter(key,value);
        }
    }

    @Resource
    QueryFactory queryFactory;

    public ResponseData del(DelReq req) throws ValidException {
        String rowId = req.getItemId();
        if (StringUtils.isEmpty(rowId)) {
            throw new ValidException(CoreErrorMessage.ITEM_ID_NOT_NULL);
        }
        int length = getRepository().del(rowId);
        if (length == 0) {
            throw new ValidException(CoreErrorMessage.ITEM_NOT_FOUND);
        }
        onDel(req);
        return ResponseData.success(length);
    }

    public ResponseData create(V req) throws ValidException {
        T t = newEntity();
        req.parse(t);
        beforeCreate(t,req);
        getRepository().save(t);
        afterCreate(t, req);
        return ResponseData.success(t);
    }

    public ResponseData page(W w) throws ValidException {
        return ResponseData.success(pageItems(w).getData());
    }

    //分页自定sql
    public String getPageSql(W w){
        String sql = getSql(w);
        return StringUtils.isEmpty(sql)?sql:null;
    }


    /**
     * 列表sql
     * @return
     */
    public String getSql(W w){return null;}
    public List getKeys(){return null;}

    public PageResult<Q> pageItems(W w) throws ValidException {
        String sql = getPageSql(w);
        PageResult page;
        if(StringUtils.isEmpty(sql)) {
            sql = getFullQuerySql(w);
            log.debug("pageItem sql={}", sql);
            onPageQuery(w, sql);
            page = pages(w, getDto(), sql);
            onAfterQuery(page, w, sql);
        }else{
            page = pages(w, getDto(), sql);
            onAfterQuery(page, w, sql);
        }
        return page;
    }

    public ResponseData list(W w) throws ValidException {
        return ResponseData.success(items(w).getData());
    }

    public ListResult items(W w) throws ValidException {
        String sql = getSql(w);
        ListResult list;
        if(!StringUtils.isEmpty(sql)) {
            list = list(w, getDto(), sql);
        }else {
            sql = getFullQuerySql(w);
            log.debug("items sql={}", sql);
            onListQuery(w, sql);
            list = list(w, getDto(), sql);
            onAfterQuery(list, w, sql);
        }
        return list;
    }

    /**
     *
     * @param w 分页请求参数
     * @param dto 视图
     * @param <P>
     * @return
     * @throws ValidException
     */
    public <P> ListResult<P> list(W w,Class dto) throws ValidException {
        String sql = getFullQuerySql(w);
        return list(w,dto,sql);
    }

    /**
     *
     * @param w 分页请求参数
     * @param dto 视图
     * @param <P> 返回分页请求结果集
     * @return
     * @throws ValidException
     */
    public <P> PageResult<P> pages(W w,Class dto) throws ValidException {
        String sql = getFullQuerySql(w);
        return pages(w,dto,sql);
    }

    /**
     *
     * @param w 分页请求参数
     * @param dto 返回视图
     * @param sql sql语句
     * @param <P>
     * @return
     * @throws ValidException
     */
    public <P> ListResult<P> list(W w,Class dto,String sql) throws ValidException {
        ListResult list = queryFactory.selectListAndKeys(sql, getQueryParameter(w),dto);
        return list;
    }

    /**
     *
     * @param w 分页请求参数
     * @param dto 分页视图
     * @param sql 执行的sql语句
     * @param <P>
     * @return
     * @throws ValidException
     */
    public <P> PageResult<P> pages(W w,Class dto,String sql) throws ValidException {
        List<String> keys = getKeys();
        PageResult<P> page = null;
        if(CollectionUtils.isEmpty(keys)) {
            page = queryFactory.selectPage(sql, getQueryParameter(w), w, dto);
        }else{
            page = queryFactory.selectPageAndKeys(sql,getQueryParameter(w),w,dto,getKeys());
        }
        return page;
    }

    /**
     *
     * @param w
     * @param dto
     * @param sql
     * @param key
     * @param <P>
     * @return
     * @throws ValidException
     */
    public <P> PageResult<P> pages(W w, Class dto, String sql, String key) throws ValidException {
        PageResult<P> page = queryFactory.selectPageAndKeys(sql, getQueryParameter(w),w,dto,key);
        return page;
    }

    /**
     * 返回列表结果集
     * @param keys 主键列表值
     * @param paramConstructor 构造参数请求
     * @return
     * @throws ValidException
     */
    public ListResult<Q> itemsByIds(List<String> keys, ParamConstructor paramConstructor) throws ValidException {
        if(CollectionUtils.isEmpty(keys)){
            return new ListResult(null,null);
        }
        String sql = getFullQuerySql(paramConstructor,null);
        ListResult<Q> list = queryFactory.selectListAndKeys(sql, query -> paramConstructor.setParameters(query), getDto());
        return list;
    }

    public void onAfterQuery(ListResult<Q> list, W w, String sql) {
        log.debug("onAfterQuery list={},w={},sql={}",list,w,sql);
    }

    public void onAfterQuery(PageResult<Q> list, W w, String sql) throws ValidException {
        log.debug("onAfterQuery list={},w={},sql={}",list,w,sql);
    }

    public T get(String rowId) throws ValidException {
        return JpaUtil.get(rowId,getRepository(),true);
    }

    public ResponseData update(V v) throws ValidException {
        String rowId = v.getRowId();
        T t = null;
        if(StringUtils.isEmpty(rowId)){
            t = newEntity();
        }else{
            t = JpaUtil.get(rowId,getRepository(),true);
        }
        T old = (T) t.clone();
        v.parse(t);
        beforeUpdate(t,old,v);
        getRepository().save(t);
        afterSave(t,v);
        return ResponseData.success();
    }

    public SortParam[] getDefaultSorts(){
        return new SortParam[]{
                SortParam.build("createTime")
        };
    }

    public QuerySort[] getSorts(W w){
        SortParam[] sorts = w.getSorts();
        if(sorts==null || sorts.length==0) {
            sorts = getDefaultSorts();
        }
        QuerySort[] querySorts = new QuerySort[sorts.length];
        for(int i=0;i<sorts.length;i++){
            SortParam sortParam = sorts[i];
            boolean flag =sortParam.getType();
            String fieldName = sortParam.getFieldName();
            Column column =  (Column) ReflectUtils.getAnnotationByReadMethod(getEntityClass(),fieldName, Column.class);
            if(column==null){
                continue;
            }
            String name = column.name();
            QuerySort querySort;
            if(flag){
                querySort = QuerySort.desc(name);
            }else{
                querySort = QuerySort.asc(name);
            }
            querySorts[i] = querySort;
        }
        return querySorts;
    }
}

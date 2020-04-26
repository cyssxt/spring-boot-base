package com.cyssxt.common.request;

import com.cyssxt.common.basedao.data.QueryFunction;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PageReq extends BaseReq {

    int pageNo=1;
    int pageSize=10;
    private SortParam[] sorts;
    List<String> rowIds;

    public PageReq() {
    }


    public PageReq(Integer pageNo, Integer pageSize) {
        if(pageNo!=null) {
            this.pageNo = pageNo;
        }
        if(pageSize!=null) {
            this.pageSize = pageSize;
        }
    }
    public Integer offset(){
        return (this.getPageNo()-1)*this.getPageSize();
    }
}

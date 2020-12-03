package com.cyssxt.common.response;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    Integer pageNo;
    Integer pageSize;
    Long totalPage;
    Long total;
    List<T> items;
    public PageResponse(List<T> items, Integer pageNo, Integer pageSize, Long total) {
        this.total = total;
        this.items = items;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
    }

    public Long getTotalPage() {
        return (long)Math.ceil(total*1.0/pageSize);
    }
}

package com.cyssxt.common.basedao.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryPage {
    Integer pageNo;
    Integer pageSize;
    public String toLimit(){
        return String.format(" %s,%s ",(pageNo-1)*pageSize,pageSize);
    }

    public static QueryPage limit(Integer num){
        return new QueryPage(1,100);
    }

    public static QueryPage build(Integer pageNo,Integer pageSize){
        return new QueryPage(pageNo,pageSize);
    }
}

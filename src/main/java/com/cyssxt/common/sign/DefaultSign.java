package com.cyssxt.common.sign;



import lombok.Data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class DefaultSign implements Sign {
    private String sign;
    private Timestamp timestamp;

    @Override
    public Map<String, Object> getParamMap() {
        Map<String,Object> map = new HashMap<>();
        map.put("timestamp",timestamp.getTime());
        return map;
    }
}

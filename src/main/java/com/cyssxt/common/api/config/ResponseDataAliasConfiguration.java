package com.cyssxt.common.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "response.alias")
@Data
public class ResponseDataAliasConfiguration {

    String retCode;
    String data;
    String retMsg;
    String extra;
    String serviceType;
    boolean valid;

    public Map<String,String> toMap(){
        Map<String, String> map = new HashMap<>();
        map.put("retCode", retCode);
        map.put("data", data);
        map.put("retMsg", retMsg);
        map.put("extra", extra);
        map.put("serviceType", serviceType);
        return map;
    }
}

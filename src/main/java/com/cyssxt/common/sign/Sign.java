package com.cyssxt.common.sign;

import java.sql.Timestamp;
import java.util.Map;

public interface Sign {
    String getSign();
    Timestamp getTimestamp();
    Map<String,Object> getParamMap();
}

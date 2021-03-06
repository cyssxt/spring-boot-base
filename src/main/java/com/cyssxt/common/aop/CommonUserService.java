package com.cyssxt.common.aop;

import com.cyssxt.common.dto.UserInfo;
import com.cyssxt.common.exception.ValidException;

public interface CommonUserService<T extends UserInfo> {
    T findById(String rowId) throws ValidException;

    String getUserId(String token);

}

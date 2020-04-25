package com.cyssxt.common.annotation.valid.impl;

import com.cyssxt.common.annotation.valid.AuthorizationValidator;
import com.cyssxt.common.dto.UserInfo;

public class DefaultAuthorizationValidator implements AuthorizationValidator {
    @Override
    public boolean check(UserInfo userInfo) {
        return false;
    }
}

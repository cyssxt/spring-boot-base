package com.cyssxt.common.annotation.valid.impl;

import com.cyssxt.common.annotation.valid.AuthorizationValidator;
import com.cyssxt.common.dto.UserInfo;

import java.lang.reflect.Method;

public abstract class BaseApiValidator implements AuthorizationValidator {

    public Method method;

    public BaseApiValidator(Method method) {
        this.method = method;
    }
}

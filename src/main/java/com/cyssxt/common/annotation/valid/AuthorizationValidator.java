package com.cyssxt.common.annotation.valid;

import com.cyssxt.common.dto.UserInfo;

public interface AuthorizationValidator {

    boolean check(UserInfo userInfo);
}

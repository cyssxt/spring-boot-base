package com.cyssxt.common.annotation;

import com.cyssxt.common.annotation.valid.impl.DefaultAuthorizationValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorization{

    boolean userRequired() default false;

    boolean isUser() default true;

    String userFieldName() default CustomHttpHeaders.U_USER_ID;

    boolean isClient() default false;

    boolean clientRequired() default false;

    String clientFieldName() default CustomHttpHeaders.U_CLIENT_ID;

    Class validator() default DefaultAuthorizationValidator.class;

}

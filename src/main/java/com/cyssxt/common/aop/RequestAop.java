package com.cyssxt.common.aop;

import com.cyssxt.common.annotation.Alias;
import com.cyssxt.common.annotation.Authorization;
import com.cyssxt.common.config.SystemConfig;
import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.response.CoreErrorMessage;
import com.cyssxt.common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
@Slf4j
@EnableConfigurationProperties(SystemConfig.class)
public class RequestAop {

    private static final String USER_TYPE = "userType";
    private static final String USER_ID = "userId";
//    private static final String SIGN_KEY = "signKey";


    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)||@annotation(org.springframework.web.bind.annotation.GetMapping)||@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void pointCut(){
    }

    @Resource
    SystemConfig systemConfig;

    @Resource
    RedisUtil redisUtil;

    /**
     * 接口拦截
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws ValidException {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Object[] objects = pjp.getArgs();
        Method method = methodSignature.getMethod();
        Class parent = method.getDeclaringClass();
        //使用类上加注解代表所有类需要授权
        Authorization authorization= (Authorization) parent.getAnnotation(Authorization.class);
        log.info("params={},method={}",objects,method.getName());
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Byte userType = null;
        String userId = null;
        if(requestAttributes!=null){
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
//            clientId = request.getHeader(CustomHttpHeaders.U_CLIENT_ID);
//            userId = request.getHeader(CustomHttpHeaders.U_USER_ID);
//            signKey = request.getHeader(CustomHttpHeaders.U_SIGN_KEY);
        }
//        redisUtil.getStringValue();
        log.info("userType={},userId={}",userType,userId);
        Parameter[] parameters = method.getParameters();
        int length = parameters.length;
        for(int i=0;i<length;i++){
            Parameter parameter = parameters[i];
            String name = parameter.getName();
            Alias alias =  parameter.getAnnotation(Alias.class);
            if(alias!=null) {
                name = alias.value();
            }
            if(USER_TYPE.equals(name)){
                objects[i] = userType;
            }else if(USER_ID.equals(name)){
                objects[i] = userId;
            }
        }
        if(authorization==null) {
             authorization = method.getDeclaredAnnotation(Authorization.class);
        }
        if(authorization!=null){
            if(StringUtils.isEmpty(userId)){
                throw new ValidException(CoreErrorMessage.USER_NOT_EXIST);
            }
        }
        try {
            return pjp.proceed(objects);
        } catch (Throwable throwable) {
            log.error("proceed={}",throwable);
            if(throwable instanceof ValidException){
                throw  (ValidException)throwable;
            }
            throw new ValidException(CoreErrorMessage.SYSTEM_ERROR);
        }
    }

}

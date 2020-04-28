package com.cyssxt.common.aop;

import com.cyssxt.common.annotation.Alias;
import com.cyssxt.common.annotation.Authorization;
import com.cyssxt.common.annotation.valid.AuthorizationValidator;
import com.cyssxt.common.annotation.valid.impl.DefaultAuthorizationValidator;
import com.cyssxt.common.api.controller.BaseController;
import com.cyssxt.common.config.SystemConfig;
import com.cyssxt.common.dto.UserInfo;
import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.request.BaseReq;
import com.cyssxt.common.response.CoreErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Aspect
@Component
@Slf4j
@EnableConfigurationProperties(SystemConfig.class)
public class RequestAop {

    private static final String USER_TYPE = "userType";
    private static final String USER_ID = "userId";
    private static final String USER = "user";
//    private static final String SIGN_KEY = "signKey";


    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)||@annotation(org.springframework.web.bind.annotation.GetMapping)||@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void pointCut() {
    }

    @Resource
    SystemConfig systemConfig;

    @Resource
    CommonUserService userService;

    /**
     * 接口拦截
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws ValidException {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Object[] objects = pjp.getArgs();
        Method method = methodSignature.getMethod();
        Class parent = method.getDeclaringClass();
        Object object = pjp.getTarget();
        Authorization authorization = null;
        Map<String, Class<? extends AuthorizationValidator>> authorizationConfig = null;
        boolean flag = false;
        if (object instanceof BaseController) {
            authorization = object.getClass().getAnnotation(Authorization.class);
            authorizationConfig = ((BaseController) object).getAuthorizationConfig();
            flag = true;
        } else {
            authorization = (Authorization) parent.getAnnotation(Authorization.class);
        }
        //使用类上加注解代表所有类需要授权

        log.debug("params={},method={}", objects, method.getName());
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Byte userType = null;
        String userId;
        String token = null;
        UserInfo userInfo;
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (!StringUtils.isEmpty(token)) {
                if (!token.startsWith("Bearer ")) {
                    throw new ValidException(CoreErrorMessage.AUTHORIZATION_ERROR);
                } else {
                    token = token.replace("Bearer ", "").trim();
                    if (StringUtils.isEmpty(token)) {
                        throw new ValidException(CoreErrorMessage.AUTHORIZATION_ERROR);
                    }
                }
            }
        }
        log.debug("token={}", token);
        if (authorization == null) {
            authorization = method.getDeclaredAnnotation(Authorization.class);
        }

        Parameter[] parameters = method.getParameters();
        int length = parameters.length;
        List<String> errors = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Object param = objects[i];
            if (param instanceof BaseReq && !StringUtils.isEmpty(token)) {
                ((BaseReq) param).setSessionId(token);
            }
            if (param instanceof BindingResult) {
                BindingResult bindingResult = (BindingResult) objects[i];
                if (bindingResult.hasErrors()) {
                    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
                    fieldErrors.forEach(fieldError -> {
                        //日志打印不符合校验的字段名和错误提示
                        log.error("error field is : {} ,message is : {}", fieldError.getField(), fieldError.getDefaultMessage());
                    });
                    for (int j = 0; j < fieldErrors.size(); j++) {
                        FieldError fieldError = fieldErrors.get(j);
                        //控制台打印不符合校验的字段名和错误提示
//                        System.out.println("error field is :"+fieldErrors.get(i).getField()+",message is :"+fieldErrors.get(i).getDefaultMessage());
                        errors.add(String.format("%s:%s", fieldError.getField(), fieldError.getDefaultMessage()));
                    }
                    throw new ValidException(CoreErrorMessage.PARAM_ERROR, errors);
                }
                break;
            }
        }
        Class<? extends AuthorizationValidator> validatorClass = null;
        if(flag){
            String methodName = method.getName();
            validatorClass = authorizationConfig.get(methodName);
        }
        if (authorization != null || validatorClass!=null) {
            if (authorization!=null) {//如果是基础类，则调用AuthorizationConfig
                Class validator = authorization.validator();
                if (validator == DefaultAuthorizationValidator.class) {
                    validatorClass = null;
                }
            }
            if (StringUtils.isEmpty(token) && validatorClass!=null) {
                throw new ValidException(CoreErrorMessage.SHOULD_LOGIN);
            }
            userId = userService.getUserId(token);
            if (StringUtils.isEmpty(userId) && validatorClass!=null) {
                throw new ValidException(CoreErrorMessage.USER_ID_NOT_NULL);
            }
            if(userId==null){
                throw new ValidException(CoreErrorMessage.USER_ID_NOT_NULL);
            }
            userId = userId.split("_")[0];
            userInfo = userService.findById(userId);
            userType = userInfo.getType();
            if (validatorClass != null) {
                try {
                    AuthorizationValidator authorizationValidator = validatorClass.newInstance();
                    authorizationValidator.check(userInfo);
                } catch (Exception e) {
                    throw new ValidException(CoreErrorMessage.VALIDATOR_INIT_ERROR);
                }
            }
//            if (validatorClass != DefaultAuthorizationValidator.class) {
//                try {
//                    AuthorizationValidator authorizationValidator =  validatorClass.newInstance();
//                    authorizationValidator.check(userInfo);
//                } catch (Exception e) {
//                    throw new ValidException(CoreErrorMessage.VALIDATOR_INIT_ERROR);
//                }
//            }
//            if (flag && authorizationConfig != null) {//如果是基础类，则调用AuthorizationConfig
//                String methodName = method.getName();
//                Class<? extends AuthorizationValidator> validatorClass = authorizationConfig.get(methodName);
//                if (validatorClass != null)
//                    try {
//                        AuthorizationValidator authorizationValidator = validatorClass.newInstance();
//                        authorizationValidator.check(userInfo);
//                    } catch (Exception e) {
//                        throw new ValidException(CoreErrorMessage.VALIDATOR_INIT_ERROR);
//                    }
//            } else {
//                Class validator = authorization.validator();
//                if (validator != DefaultAuthorizationValidator.class) {
//                    try {
//                        AuthorizationValidator authorizationValidator = (AuthorizationValidator) validator.newInstance();
//                        authorizationValidator.check(userInfo);
//                    } catch (Exception e) {
//                        throw new ValidException(CoreErrorMessage.VALIDATOR_INIT_ERROR);
//                    }
//                }
//            }
            for (int j = 0; j < length; j++) {
                Parameter parameter = parameters[j];
                String name = parameter.getName();
                Alias alias = parameter.getAnnotation(Alias.class);
                if (alias != null) {
                    name = alias.value();
                }
                if (USER_TYPE.equals(name)) {
                    objects[j] = userType;
                } else if (USER_ID.equals(name)) {
                    objects[j] = userId;
                } else if (USER.equals(name)) {
                    objects[j] = userInfo;
                }
            }
            if (userInfo == null) {
                throw new ValidException(CoreErrorMessage.TOKEN_NOT_VALID);
            }
        }
        try {
            return pjp.proceed(objects);
        } catch (Throwable throwable) {
            log.error("proceed={}", throwable);
            if (throwable instanceof ValidException) {
                throw (ValidException) throwable;
            }
            throw new ValidException(CoreErrorMessage.SYSTEM_ERROR);
        }
    }

}

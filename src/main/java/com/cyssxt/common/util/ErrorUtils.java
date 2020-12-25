package com.cyssxt.common.util;

import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.response.CoreErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ErrorUtils {

    public static void checkErrors(BindingResult bindingResult) throws ValidException {
        if (bindingResult.hasErrors()) {
            throw new ValidException(CoreErrorMessage.PARAM_ERROR, parseErrors(bindingResult.getFieldErrors()));
        }
    }

    public static List<String> parseErrors(List<FieldError> fieldErrors){
        List<String> errors = new ArrayList<>();
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
        return errors;
    }
}

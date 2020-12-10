package com.cyssxt.common.advice;

import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.response.CoreErrorMessage;
import com.cyssxt.common.response.ErrorMessage;
import com.cyssxt.common.response.ResponseData;
import com.cyssxt.common.util.ErrorUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ValidException.class)
    @ResponseBody
    public ResponseEntity valid(ValidException e) {
        ErrorMessage errorMessage = e.getErrorMessage();
        return ResponseEntity.status(errorMessage.getStatusCode()).body(ResponseData.fail(e));
    }

    //参数异常校验
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseData paramError(MethodArgumentNotValidException e) {
        ResponseData responseData = ResponseData.fail(CoreErrorMessage.PARAM_ERROR);
        responseData.setData(ErrorUtils.parseErrors(e.getBindingResult().getFieldErrors()));
        return responseData;
    }
}

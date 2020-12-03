package com.cyssxt.common.advice;

import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.response.ErrorMessage;
import com.cyssxt.common.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ValidException.class)
    @ResponseBody
    public ResponseEntity valid(ValidException e){
        ErrorMessage errorMessage = e.getErrorMessage();
        return ResponseEntity.status(errorMessage.getStatusCode()).body(ResponseData.fail(e));
    }
}

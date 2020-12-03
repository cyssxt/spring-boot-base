package com.cyssxt.common.exception;

import com.cyssxt.common.response.ErrorMessage;
import lombok.Data;

import java.util.List;

@Data
public class ValidException extends Throwable{
    ErrorMessage errorMessage;
    Object data;
    List<String> errors;

    public ValidException(ErrorMessage errorMessage) {
        super(errorMessage.getMsg());
        this.errorMessage = errorMessage;
    }

    public ValidException(ErrorMessage errorMessage,Object t) {
        super(errorMessage.getMsg());
        this.errorMessage = errorMessage;
        this.data = t;
    }

    public ValidException(ErrorMessage errorMessage,List<String> t) {
        super(errorMessage.getMsg());
        this.errorMessage = errorMessage;
        this.errors = t;
    }

}

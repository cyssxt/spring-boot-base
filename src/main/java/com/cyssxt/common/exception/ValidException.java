package com.cyssxt.common.exception;

import com.cyssxt.common.response.ErrorMessage;
import lombok.Data;

@Data
public class ValidException extends Throwable{
    ErrorMessage errorMessage;
    Object data;

    public ValidException(ErrorMessage errorMessage) {
        super(errorMessage.getMsg());
        this.errorMessage = errorMessage;
    }

    public ValidException(ErrorMessage errorMessage,Object t) {
        super(errorMessage.getMsg());
        this.errorMessage = errorMessage;
        this.data = t;
    }
}

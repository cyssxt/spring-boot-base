package com.cyssxt.common.request;

import lombok.Data;

@Data
public class BaseRequest<T> {
    String origin;

    T data;
}

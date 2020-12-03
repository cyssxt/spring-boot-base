package com.cyssxt.common.api.controller.req;

import com.cyssxt.common.request.BaseReq;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class InfoReq extends BaseReq {
    @NotEmpty
    @NotNull
    String rowId;
}

package com.cyssxt.common.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateReq extends BaseReq{
    String rowId;
}

package com.cyssxt.common.api.controller.req;

import com.cyssxt.common.request.CreateReq;
import lombok.Data;

@Data
public class AuditCreateReq extends CreateReq {

    Byte audit = 0;
    String auditMsg = "";
}

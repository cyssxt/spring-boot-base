package com.cyssxt.common.api.controller.req;

import lombok.Data;

@Data
public class AuditReq {
    Byte audit;
    String auditMsg;
    String contentId;
}

package com.cyssxt.common.api.controller;

import com.cyssxt.common.annotation.Authorization;
import com.cyssxt.common.api.controller.req.AuditCreateReq;
import com.cyssxt.common.api.controller.req.AuditCreateTimeDto;
import com.cyssxt.common.api.controller.req.AuditPageReq;
import com.cyssxt.common.api.controller.req.AuditReq;
import com.cyssxt.common.api.service.AuditService;
import com.cyssxt.common.entity.AuditEntity;
import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.response.ResponseData;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

public abstract class AuditController<T extends AuditEntity, V extends AuditCreateReq, Q extends AuditCreateTimeDto, W extends AuditPageReq> extends BaseController<T,V,Q,W> {

    public abstract AuditService getCommonService();

    @RequestMapping(value="audit")
    public ResponseData audit(@Valid @RequestBody AuditReq req, BindingResult result) throws ValidException {
        return getCommonService().audit(req);
    }
}

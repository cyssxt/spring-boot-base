package com.cyssxt.common.api.service;

import com.cyssxt.common.api.controller.req.AuditCreateReq;
import com.cyssxt.common.api.controller.req.AuditCreateTimeDto;
import com.cyssxt.common.api.controller.req.AuditPageReq;
import com.cyssxt.common.api.controller.req.AuditReq;
import com.cyssxt.common.basedao.util.JpaUtil;
import com.cyssxt.common.entity.AuditEntity;
import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.response.CoreErrorMessage;
import com.cyssxt.common.response.ResponseData;
import lombok.Data;
import javax.validation.Valid;

@Data
public abstract class AuditService<T extends AuditEntity, V extends AuditCreateReq, Q extends AuditCreateTimeDto, W extends AuditPageReq>  extends BaseService<T,V,Q,W> {


    public ResponseData audit(@Valid AuditReq req) throws ValidException {
        T t = JpaUtil.get(req.getContentId(),getRepository(), CoreErrorMessage.ITEM_NOT_FOUND);
        t.setAudit(req.getAudit());
        t.setAuditMsg(req.getAuditMsg());
        getRepository().save(t);
        return ResponseData.success();
    }
}

package com.cyssxt.common.api.controller;

import com.cyssxt.common.dto.CreateTimeDto;
import com.cyssxt.common.entity.AuditEntity;
import com.cyssxt.common.request.CreateReq;
import com.cyssxt.common.request.PageReq;

public abstract class AuditController<V extends AuditEntity,C extends CreateReq,T extends CreateTimeDto,P extends PageReq> extends BaseController<V,C,T ,P> {

}

package com.cyssxt.common.api.service;

import com.cyssxt.common.dto.CreateTimeDto;
import com.cyssxt.common.entity.AuditEntity;
import com.cyssxt.common.request.CreateReq;
import com.cyssxt.common.request.PageReq;
import lombok.Data;

@Data
public abstract class AuditService<T extends AuditEntity, V extends CreateReq, Q extends CreateTimeDto, W extends PageReq> extends BaseService<T,V,Q,W> {



}

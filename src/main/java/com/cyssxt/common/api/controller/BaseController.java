package com.cyssxt.common.api.controller;

import com.cyssxt.common.api.service.BaseService;
import com.cyssxt.common.dto.BaseDto;
import com.cyssxt.common.dto.CreateTimeDto;
import com.cyssxt.common.entity.BaseEntity;
import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.request.CreateReq;
import com.cyssxt.common.request.DelReq;
import com.cyssxt.common.request.PageReq;
import com.cyssxt.common.response.ResponseData;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

public abstract class BaseController<T extends BaseEntity, V extends CreateReq, Q extends CreateTimeDto, W extends PageReq> {

    public abstract BaseService getCommonService();

    @RequestMapping(value="/list",method = RequestMethod.POST)
    public ResponseData list(@Valid @RequestBody W req, BindingResult result) throws ValidException {
        return  getCommonService().list(req);
    }

//    @RequestMapping(value="/{contentId}",method = RequestMethod.POST)
//    public ResponseData list(@Valid @PathVariable("contentId")String contentId) throws ValidException {
//        return  getCommonService().infoResult(contentId);
//    }

    @RequestMapping(value="/page",method = RequestMethod.POST)
    public ResponseData page(@Valid @RequestBody W req, BindingResult result) throws ValidException {
        return  getCommonService().page(req);
    }

    @RequestMapping(value="/update",method = RequestMethod.POST)
    public ResponseData update(@Valid @RequestBody V req, BindingResult result) throws ValidException {
        return  getCommonService().update(req);
    }

    @RequestMapping(value="/del",method = RequestMethod.POST)
    public ResponseData del(@Valid @RequestBody DelReq req, BindingResult result) throws ValidException {
        return  getCommonService().del(req);
    }
}

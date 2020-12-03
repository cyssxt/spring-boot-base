package com.cyssxt.common.api.controller;

import com.cyssxt.common.annotation.Alias;
import com.cyssxt.common.annotation.Authorization;
import com.cyssxt.common.annotation.valid.AuthorizationValidator;
import com.cyssxt.common.annotation.valid.impl.BaseApiValidator;
import com.cyssxt.common.annotation.valid.impl.DefaultAuthorizationValidator;
import com.cyssxt.common.api.controller.req.InfoReq;
import com.cyssxt.common.api.service.BaseService;
import com.cyssxt.common.dto.CreateTimeDto;
import com.cyssxt.common.entity.BaseEntity;
import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.request.CreateReq;
import com.cyssxt.common.request.DelReq;
import com.cyssxt.common.request.PageReq;
import com.cyssxt.common.response.ResponseData;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseController<T extends BaseEntity, V extends CreateReq, Q extends CreateTimeDto, W extends PageReq> {

    protected final static String LIST = "list";
    protected final static String PAGE = "page";
    protected final static String UPDATE = "update";
    protected final static String DEL = "del";

    public abstract BaseService getCommonService();

    public interface AuthorizationConfig{
        Map<String,Class<? extends AuthorizationValidator>> getAuthorizations();
    }

    @RequestMapping(value="/list",method = RequestMethod.POST)
    public ResponseData list(@Valid @RequestBody W req, BindingResult result) throws ValidException {
        return  getCommonService().list(req);
    }

    @RequestMapping(value="/info",method = RequestMethod.POST)
    public ResponseData list(@Valid @RequestBody InfoReq req) throws ValidException {
        return  getCommonService().infoResult(req.getRowId());
    }
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

    public Map<String,Class<? extends AuthorizationValidator>> getAuthorizationConfig(){
        Map<String,Class<? extends AuthorizationValidator>> map = new HashMap<>();
        map.put(LIST,null);
        map.put(PAGE,null);
        map.put(UPDATE,null);
        map.put(DEL,null);
        return map;
    }
}

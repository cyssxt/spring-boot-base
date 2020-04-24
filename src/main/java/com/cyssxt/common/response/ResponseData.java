package com.cyssxt.common.response;

import lombok.Data;

@Data
public class ResponseData<T> {
    Integer retCode;
    String retMsg;
    T data;
    String serviceType;
    String extra;

    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

//    public void success(){
//        this.retCode = 0;
//        this.retMsg = "SUCCESS";
//    }
    public static ResponseData fail(){
        ResponseData responseData = new ResponseData();
        responseData.retCode = -1;
        responseData.retMsg = "fail";
        return responseData;
    }

    public static ResponseData fail(String message){
        ResponseData responseData = new ResponseData();
        responseData.retCode = -1;
        responseData.retMsg = message;
        return responseData;
    }

    public static ResponseData fail(ErrorMessage errorMessage,Object data){
        ResponseData responseData = new ResponseData(errorMessage);
        responseData.data = data;
        return responseData;
    }
    public static ResponseData fail(ErrorMessage errorMessage){
        return fail(errorMessage,null);
    }

    public static ResponseData success(){
        return success(null);
    }
    public static ResponseData success(Object object){
        ResponseData responseData = new ResponseData();
        responseData.retCode = 0;
        responseData.retMsg = "SUCCESS";
        responseData.data = object;
        return responseData;
    }

    public ResponseData(){
    }
    public ResponseData(ErrorMessage errorMessage){
        this.retCode = errorMessage.getCode();
        this.retMsg = errorMessage.getMsg();
    }
    public static ResponseData getInstance(){
        ResponseData responseData = new ResponseData();
        responseData.success();
        return responseData;
    }

    public static ResponseData getInstance(ErrorMessage errorMessage) {
        ResponseData responseData = new ResponseData(errorMessage);
        return responseData;
    }

    public void parseErrorMessage(ErrorMessage errorMessage) {
        this.retCode = errorMessage.getCode();
        this.retMsg = errorMessage.getMsg();
    }
}

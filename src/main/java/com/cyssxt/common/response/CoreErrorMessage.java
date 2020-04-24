package com.cyssxt.common.response;

public enum CoreErrorMessage implements ErrorMessage{
    SYSTEM_ERROR(9999999,"系统异常"),
    USER_NOT_EXIST(10000001, "用户不存在"),
    API_GET_DTO_ERROR(10000002,"API视图不存在" ),
    NEW_INSTANCE_ERROR(10000003, "API类实例化失败"),
    ITEM_ID_NOT_NULL(10000004, "内容Id为空"),
    ITEM_NOT_FOUND(10000005, "内容找不到"),
    QUERY_ENTITY_MAX_HAS_ENTITY_ANNOTATION(10000006, "查找表名失败"),
    ENCRYPT_ERROR(10000007, "解密失败"),
    PARAM_NOT_TREE(10000008, "参数不是Tree"),
    DECRYPT_ERROR(10000009, "解密失败"),
    CANNOT_FIND_FIELD(10000010, "找不到字段"),
    CANNOT_FIND_FIELD_WITH_ANNOTATION(10000011, "找不到注解字段"),
    COPY_ERROR(10000012, "拷贝异常"),
    CODE_NOT_EXIST(10000013,"验证码不存在" );
    private Integer retCode;
    private String msg;
    private int status=200;
    CoreErrorMessage(Integer retCode, String msg){
        this.retCode = retCode;
        this.msg = msg;
    }
    CoreErrorMessage(Integer retCode, String msg,int status) {
        this.retCode = retCode;
        this.msg = msg;
        this.status = status;
    };

    @Override
    public Integer getCode() {
        return this.retCode;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public int getStatusCode() {
        return status;
    }
}

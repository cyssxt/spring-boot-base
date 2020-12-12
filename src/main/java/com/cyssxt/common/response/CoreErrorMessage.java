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
    CODE_NOT_EXIST(10000013,"验证码不存在" ),
    TOKEN_NOT_VALID(10000014, "token无效"),
    USER_ID_NOT_NULL(10000015, "暂未登录或用户已被删除"),
    SHOULD_LOGIN(10000016, "请先登录"),
    PARAM_ERROR(10000017, "参数错误"),
    AUTHORIZATION_ERROR(10000018, "Authorization头错误"),
    VALIDATOR_INIT_ERROR(10000019, "校验器处理失败"),
    MD5_NOT_EXIST(10000020, "算法不存在"),
    DTO_CLASS_NOT_EXIST(10000021, "视图类不存在"),
    FIELD_IS_NULL(10000022, "字段名不能为空"),
    FIELD_NOT_EXIST_WRITE_METHOD(10000023, "字段不存在对应的写方法");
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

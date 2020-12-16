# spring-boot-base
描述基于Spring boot的快速API开放框架
## 前言：
    该框架基于spring data jpa 作为数据库中间件
1. 安装
    ````
    <dependency>
        <groupId>com.cyssxt</groupId>
        <artifactId>spring-boot-base</artifactId>
        <version>${version}</version>
    </dependency>
    ````

2. 讲发布包引入到Application启动注解的扫描包参数中. eg.
    ````
    @SpringBootApplication(scanBasePackages = {"com.cyssxt"})
    ````
3. 接口消息通用返回
<font color="#ff0000">
    PS: 本框架通用消息体为
</font>
    ````
   {
           "data":自定返回结构
           "errors": null,//接口错误列表
           "extra": null,//额外附加信息
           "retCode": 0,//错误吗
           "retMsg": "SUCCESS",//返回消息
           "serviceType": null //服务类型
       }
   ````
    3.1 data构造示例：
    ````
        UserInfo userInfo = new UserInfo();
        ResponseData.success(userInfo)
    ````

   3.2 errors：
    ````
   @Data
   public class LoginReq extends BaseReq {
       String phone;
       @NotEmpty
       @NotNull
       String secret; //当前此字段为空或者为null时会在errors中添加信息
   }
   ````
    
   3.3 extra示例：
    ````
     responseData.setExtra(3); //设置额外信息 ，非必需 无必要
    ````

   3.4 retCode和retMsg示例
   
   TIP: 步骤1：枚举类继承ErrorMessage接口

    ````aidl
    import com.cyssxt.common.response.ErrorMessage;
    import com.cyssxt.common.response.ErrorMessage;
        
    public enum  CarErrorMessage implements ErrorMessage {
        CODE_NOT_EXIST(2000001, "验证码不存在");
            private Integer retCode;
        private String msg;
        private int status=200;
    
        CarErrorMessage(Integer retCode, String msg){
            this.retCode = retCode;
            this.msg = msg;
        }
        @Override
        public Integer getCode() {
            return retCode;//返回的状态值
        }
    
        @Override
        public String getMsg() {
            return msg; //返回的retMsg
        }
    
        @Override
        public int getStatusCode() {
            return 200; //状态吗
        }
    }
    ````
    3.5 返回状态吗或者抛出异常
    ````
    public ResponseData findCar(){
        throw new ValidException(CarErrorMessage.CODE_NOT_EXIST);
    }
    ````
    OR
    ````
    public ResponseData findCar(){
        return ResponseData fail(CarErrorMessage.CODE_NOT_EXIST);
    }
    ````    

4. CURD
    该框架提供快速开发api的方法（提供增、删、改、查）

    4.1 继承com.cyssxt.common.api.controller类即可实现通用增删改查方法
    ````
    public abstract class BaseController<T extends BaseEntity, V extends CreateReq, Q extends CreateTimeDto, W extends PageReq> {
     @RequestMapping(value="/list",method = RequestMethod.POST)
       public ResponseData list(@Valid @RequestBody W req, BindingResult result) throws ValidException;//全量列表返回
    
       @RequestMapping(value="/info",method = RequestMethod.POST) 
       public ResponseData list(@Valid @RequestBody InfoReq req) throws ValidException; //查询单个详情
       @RequestMapping(value="/page",method = RequestMethod.POST)
       public ResponseData page(@Valid @RequestBody W req, BindingResult result) throws ValidException;//分页查询
    
       @RequestMapping(value="/update",method = RequestMethod.POST)
       public ResponseData update(@Valid @RequestBody V req, BindingResult result) throws ValidException;//单个更新
    
       @RequestMapping(value="/del",method = RequestMethod.POST)
       public ResponseData del(@Valid @RequestBody DelReq req, BindingResult result) throws ValidException; //单个删除
    }
    ````
   4.2 范型描述
     ````aidl
    T: 实体类 jpa实体类继承此类
    V:  创建范型，主要用于创建一个实体的时候用到的消息体结构
    Q：接口返回的视图类
    W: 分页请求参数类
    ````
   4.3 自定义查询
   ````aidl
   @Reource
   QueryFactory queryFactory;
   ListResult<ProvinceDto> provinceDtoList =  queryFactory.selectListByKeys(ProvinceEntity.class,new QueryParam[]{
                   new QueryParam("del_flag",false)
           }, ProvinceDto.class);//查询自定信息，ProvinceEntity.class为实体类
   PageResult<ADto> listResult = queryFactory.selectPageAndKeys("select * from student A ",getQueryParameter(req),req,ADto.class,getKeys());//通过sql查询数据 ADto.class为接口返回视图类
   ````
5. 全局token校验
    自定义实现CommonUserService类
    示例：
    ````aidl
        @Override
        public UserInfo findById(String userId) throws ValidException {
            UserEntity userEntity = JpaUtil.get(userId,userRepository,CarErrorMessage.USER_NOT_EXIST);
            if(userEntity!=null && CommonUtil.isTrue(userEntity.getDelFlag())){
                throw new ValidException(CoreErrorMessage.USER_ID_NOT_NULL);
            }
            return userEntity;
        }
    
        @Override
        public String getUserId(String token) {
            String userId = redisUtil.getStringValue(getSessionKey(token));
            return userId;
        }
    ````

6. 参数校验
    ````aidl
    public class UserController {
        @Resource
        UserService userService;
    
        @RequestMapping(value="login")
        public ResponseData login(@Valid @RequestBody LoginReq req) throws ValidException;// @Valid注解支持hibernate validator
    
    ````
7. 授权校验
    ````aidl
    
        @RequestMapping(value = "update")
        @Authorization//加了此注解表示接口需要授权
        // @Alias("userId") String userId,@Alias("user")  userId表示带入userid信息 user表示带入用户信息****
        public ResponseData update(@Valid @RequestBody UserUpdateReq req, BindingResult result, @Alias("userId") String userId,@Alias("user") UserEntity user) throws ValidException ;
    ````
8. Thanks For
1) Spring boot
2) Lombok

# spring-boot-base
1. 在启动的Application增加包扫描
<pre><code>
@SpringBootApplication(scanBasePackages = {"com.cyssxt"})
</code></pre>
这个组件帮助我们迅速开发基于spring boot的api服务。
<font face="#ff0000">  
    PS: 本框架通用消息体为
</font>
<pre><code>
{
    "data":自定返回结构
    "errors": null,//接口错误列表
    "extra": null,//额外附加信息
    "retCode": 0,//错误吗
    "retMsg": "SUCCESS",//返回消息
    "serviceType": null //服务类型
}
</code></pre>
###  1) data构造示例：<pre><code>
UserInfo userInfo = new UserInfo();
ResponseData.success(userInfo)
</code></pre>

####  2) errors：<pre>
<code>
@Data
public class LoginReq extends BaseReq {
    String phone;
    @NotEmpty
    @NotNull
    String secret; //当前此字段为空或者为null时会在errors中添加信息
}
</code>
</pre>

### 3) extra示例：<pre><code>
 responseData.setExtra(3); //设置额外信息 ，非必需 无必要
</code></pre>

###  4) retCode和retMsg示例
#### 步骤1：枚举类继承ErrorMessage接口
 <pre>
 <code>
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
</code>
</pre>
#### 步骤2：返回状态吗或者抛出异常
<pre><code>
public ResponseData findCar(){
    throw new ValidException(CarErrorMessage.CODE_NOT_EXIST);
}

</code></pre>
OR
<pre><code>
public ResponseData findCar(){
    return ResponseData fail(CarErrorMessage.CODE_NOT_EXIST);
}
</code></pre>
5. 快速实现增删改查
   ##### 继承BaseController方法即可实现通用增删改查方法
6. 返回固定结构消息体
    我们可以使用ResponseData来构建基础的json返回消息体。
7. 全局token校验
    自定义实现CommonUserService类
    示例：
    <pre><code>
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
</code></pre>
4. 

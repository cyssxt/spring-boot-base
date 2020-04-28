package com.cyssxt.common.api.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.*;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.cyssxt.common.config.JSONEnum;
import com.cyssxt.common.response.ResponseData;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Configuration
public class JSONConfig {

    @Resource
    ResponseDataAliasConfiguration configuration;

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters(){
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        // 2.添加fastjson的配置信息，比如: 是否需要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue, SerializerFeature.PrettyFormat, SerializerFeature.DisableCircularReferenceDetect);
//        fastJsonConfig.setSerializeFilters(new PropertyPreFilter(){
//            @Override
//            public boolean apply(JSONSerializer serializer, Object object, String name) {
//                return true;
//            }
//        });
//        Map<String,String> map = new HashMap<>();
//        map.put("retCode","code");
//        map.put("data","bean");
//        map.put("retMsg","msg");
        //增加别名
        if(configuration!=null && configuration.isValid() ) {
            fastJsonConfig.getSerializeConfig().put(ResponseData.class, new JavaBeanSerializer(ResponseData.class, configuration.toMap()));
        }
//        fastJsonConfig.getSerializeFilters().
        fastJsonConfig.getSerializeConfig().put(Timestamp.class,new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        fastJsonConfig.setSerializeFilters(new ValueFilter() {
            @Override
            public Object process(Object o, String s, Object o1) {
                if(o1 instanceof JSONEnum){
                    JSONEnum jsonEnum = (JSONEnum) o1;
                    byte value = jsonEnum.getValue();
                    String msg = jsonEnum.getMsg();
                    HashMap result = new HashMap();
                    result.put("name",msg);
                    result.put("value",value);
                    return  result;
                }
                return o1 ;
            }
        });
        // 3.在converter中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        // 4.将converter赋值给HttpMessageConverter
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        // 5.返回HttpMessageConverters对象
        return new HttpMessageConverters(fastConverter);
    }

}

package com.cyssxt.common.config;


import lombok.Data;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "system")
@Data
public class SystemConfig  implements BeanClassLoaderAware, InitializingBean {

    String signSecret="AO3fKuJpDgTsrplX";

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

}

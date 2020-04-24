package com.cyssxt.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Slf4j
@Configuration
public class EmbeddedServletContainerAutoConfiguration {


    @Value("${server.tomcat.docbase:/tmp/springboot}")
    private  String tomcatBaseDirectory;

    @Value("${server.https.enable:false}")
    private boolean httpsEnable;

    @Value("${http.port:8080}")
    private  Integer httpPort;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcatEmbeddedServletContainerFactory = new TomcatServletWebServerFactory();
        log.info("tomcatEmbeddedServletContainerFactory:{}",this.tomcatBaseDirectory);
        File file =new File(this.tomcatBaseDirectory);
        if(!file.exists()){
            file.mkdirs();
        }
        tomcatEmbeddedServletContainerFactory.setDocumentRoot(file);
        tomcatEmbeddedServletContainerFactory.setBaseDirectory(file);
        if(httpsEnable) {
            tomcatEmbeddedServletContainerFactory.addAdditionalTomcatConnectors(createStandardConnector());
        }
        return tomcatEmbeddedServletContainerFactory;
    }

    private Connector createStandardConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setPort(httpPort);
        return connector;
    }

}

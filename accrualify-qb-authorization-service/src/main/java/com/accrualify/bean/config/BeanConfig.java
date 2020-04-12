package com.accrualify.bean.config;

import com.accrualify.resourses.NSAccessTokenEndPointImpl;
import com.accrualify.resourses.QBAccessTokenEndPointImpl;
import com.accrualify.util.CustomPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class BeanConfig {

    @Bean(name = "quickbookAccessTokenEndPointImpl")
    QBAccessTokenEndPointImpl qbAccessTokenEndPoint(){
        return new QBAccessTokenEndPointImpl();
    }

    @Bean(name = "netsuiteAccessTokenEndPointImpl")
    NSAccessTokenEndPointImpl nsAccessTokenEndPoint(){return new NSAccessTokenEndPointImpl();}

    @Bean(name = "bCryptPasswordEncoder")
    BCryptPasswordEncoder bCryptPasswordEncoder(){return new BCryptPasswordEncoder();}

    @Bean(name = "customPasswordEncoder")
    CustomPasswordEncoder customPasswordEncoder(){return new CustomPasswordEncoder();}


    /*@Bean
    public Server rsServer() {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setAddress("/");
        endpoint.setServiceClass(QBLoginEndPointImpl.class);
        // Register 2 JAX-RS root resources supporting "/sayHello/{id}" and "/sayHello2/{id}" relative paths
        endpoint.setServiceBeans(Arrays.<Object>asList(new QBLoginEndPointImpl()));
        return endpoint.create();
    }*/

}

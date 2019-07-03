package com.zhuguang.zhou.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(value = "com.zhuguang.zhou")
public class HttpConfig {

    @Bean("httpTemplate")
    public RestTemplate restTemplate (@Qualifier("commonHttpRequest") SimpleClientHttpRequestFactory requestFactory) {
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }

    @Bean("commonHttpRequest")
    public SimpleClientHttpRequestFactory simpleClientHttpRequestFactory () {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        requestFactory.setConnectTimeout(2000);
        requestFactory.setReadTimeout(4000);
        return requestFactory;
    }

}

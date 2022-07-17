package com.uplus.searchservice.feignclient.info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import feign.Target;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Target target = requestTemplate.feignTarget();
            String feignName = target.name();           
            if ("typocorrect".equals(feignName)) {
                requestTemplate.header(TypoCorrectInfo.NAVERCLIENTIDHEADER, TypoCorrectInfo.CLIENTID);
                requestTemplate.header(TypoCorrectInfo.NAVERCLIENTSECRETHEADER, TypoCorrectInfo.CLIENTSECRET);
            } 
           
        };
    }

}

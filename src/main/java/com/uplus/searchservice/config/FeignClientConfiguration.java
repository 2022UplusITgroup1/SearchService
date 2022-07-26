package com.uplus.searchservice.config;

import feign.RequestInterceptor;
import feign.Target;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

package com.uplus.searchservice.feignclient;

import com.uplus.searchservice.config.TypoCorrectInfo;
import com.uplus.searchservice.dto.response.TypoCorrectResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="typocorrect", url = TypoCorrectInfo.NAVEROPENAPIURL, configuration = FeignClientProperties.FeignClientConfiguration.class)
public interface TypoCorrectClient {
    @GetMapping
    TypoCorrectResponseDto getCorrectString(@RequestParam(value = "query") String query);
}

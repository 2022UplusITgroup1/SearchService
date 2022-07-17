package com.uplus.searchservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uplus.searchservice.dto.TypoCorrectResponseDto;
import com.uplus.searchservice.feignclient.info.FeignClientConfiguration;
import com.uplus.searchservice.feignclient.info.TypoCorrectInfo;


@FeignClient(name="typocorrect", url = TypoCorrectInfo.NAVEROPENAPIURL, configuration = FeignClientConfiguration.class)
public interface TypoCorrectClient {
    
    @GetMapping
    TypoCorrectResponseDto getCorrectString(@RequestParam(value = "query") String query);
}
